package com.example.distributedlockswithspringintegration;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ReservationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Reservation> rowMapper = (rs, i) -> new Reservation(rs.getInt("id"), rs.getString("name"));

    Optional<Reservation> findById(Integer id) {
        List<Reservation> reservationList = jdbcTemplate.query("select * from reservation where id = ?", this.rowMapper, id);
        return reservationList.isEmpty() ? Optional.empty() : Optional.of(reservationList.iterator().next());
    }

    Reservation save(Reservation reservation) {
        Assert.isTrue(reservation.getId() != null && reservation.getId() != 0);
        jdbcTemplate.execute("update reservation set name = ? where id = ?;", (PreparedStatementCallback<Reservation>) preparedStatement -> {
            preparedStatement.setString(1, reservation.getName());
            preparedStatement.setInt(2, reservation.getId());
            preparedStatement.execute();
            return findById(reservation.getId()).get();
        });
        return null;
    }

    Collection<Reservation> findAll() {
        return jdbcTemplate.query("select * from reservation", this.rowMapper);
    }
}
