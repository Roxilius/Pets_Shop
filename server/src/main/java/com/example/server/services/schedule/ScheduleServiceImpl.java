package com.example.server.services.schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.server.data_transfer_object.schedule.DMYResponse;
import com.example.server.data_transfer_object.schedule.DeviationResponse;
import com.example.server.data_transfer_object.schedule.IncomeExpensesResponse;
import com.example.server.models.Topup;
import com.example.server.models.Transaction;
import com.example.server.models.Users;
import com.example.server.repositorys.TopupRepository;
import com.example.server.repositorys.TransactionRepository;
import com.example.server.repositorys.UsersRepository;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TopupRepository topupRepository;

    @Override
    public DMYResponse daily() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String day = currentTime.format(formatter);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = usersRepository.findUsersByEmail(auth.getName());

        List<Transaction> expenses = transactionRepository.daily(user,
                LocalDateTime.of(currentTime.getYear(), currentTime.getMonth(), currentTime.getDayOfMonth(), 00, 00, 0),
                LocalDateTime.of(currentTime.getYear(), currentTime.getMonth(), currentTime.getDayOfMonth(), 23, 59,
                        59));
        Integer totalExpenses = 0;
        for (Transaction transaction : expenses) {
            totalExpenses += transaction.getTotalAmount();
        }

        List<Topup> income = topupRepository.daily(user,
                LocalDateTime.of(currentTime.getYear(), currentTime.getMonth(), currentTime.getDayOfMonth(), 00, 00, 0),
                LocalDateTime.of(currentTime.getYear(), currentTime.getMonth(), currentTime.getDayOfMonth(), 23, 59,
                        59));
        Integer totalIncome = 0;
        for (Topup topup : income) {
            totalIncome += topup.getAmount();
        }

        DMYResponse daily = new DMYResponse();
        daily.setPeriode(day);
        daily.setTotalExpenses(totalExpenses);
        daily.setTotalIncome(totalIncome);
        return daily;
    }

    @Override
    public DMYResponse monthly() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
        String month = currentTime.format(formatter);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = usersRepository.findUsersByEmail(auth.getName());

        List<Transaction> expenses = transactionRepository.monthly(user, month);
        Integer totalExpenses = 0;
        for (Transaction transaction : expenses) {
            totalExpenses += transaction.getTotalAmount();
        }

        List<Topup> income = topupRepository.monthly(user, month);
        Integer totalIncome = 0;
        for (Topup topup : income) {
            totalIncome += topup.getAmount();
        }

        DMYResponse mountly = new DMYResponse();
        formatter = DateTimeFormatter.ofPattern("yyyy, MMMM");
        month = currentTime.format(formatter);
        mountly.setPeriode(month);
        mountly.setTotalExpenses(totalExpenses);
        mountly.setTotalIncome(totalIncome);
        return mountly;
    }

    @Override
    public DMYResponse annual() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        String year = currentTime.format(formatter);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = usersRepository.findUsersByEmail(auth.getName());

        List<Transaction> expenses = transactionRepository.annual(user, year);
        Integer totalExpenses = 0;
        for (Transaction transaction : expenses) {
            totalExpenses += transaction.getTotalAmount();
        }

        List<Topup> income = topupRepository.annual(user, year);
        Integer totalIncome = 0;
        for (Topup topup : income) {
            totalIncome += topup.getAmount();
        }

        DMYResponse annual = new DMYResponse();
        annual.setPeriode(year);
        annual.setTotalExpenses(totalExpenses);
        annual.setTotalIncome(totalIncome);
        return annual;
    }

    @Override
    public DeviationResponse deviation(LocalDate startDate, LocalDate endDate) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = usersRepository.findUsersByEmail(auth.getName());

        List<Transaction> expenses = transactionRepository.expenses(user,
            LocalDateTime.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth(), 00, 00, 00),
            LocalDateTime.of(endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth(), 23, 59, 59));
        Integer totalExpenses = 0;
        for (Transaction transaction : expenses) {
            totalExpenses += transaction.getTotalAmount();
        }

        List<Topup> income = topupRepository.income(user,
            LocalDateTime.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth(), 00, 00, 00),
            LocalDateTime.of(endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth(), 23, 59, 59));
        Integer totalIncome = 0;
        for (Topup topup : income) {
            totalIncome += topup.getAmount();
        }
        DeviationResponse response = new DeviationResponse();
        response.setDeviation(totalIncome - totalExpenses);
        response.setTotalExpenses(totalExpenses);
        response.setTotalIncome(totalIncome);
        if (response.getDeviation() > 0) {
            response.setConclusion("Good");
        } else if (response.getDeviation() == 0){
            response.setConclusion("Warning");
        } else if(response.getDeviation() < 0){
            response.setConclusion("Danger");
        }
        return response;
    }

    @Override
    public IncomeExpensesResponse incomeExpenses(LocalDate startDate, LocalDate endDate) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = usersRepository.findUsersByEmail(auth.getName());

        List<Transaction> expenses = transactionRepository.expenses(user,
            LocalDateTime.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth(), 00, 00, 00),
            LocalDateTime.of(endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth(), 23, 59, 59));
        Integer totalExpenses = 0;
        for (Transaction transaction : expenses) {
            totalExpenses += transaction.getTotalAmount();
        }

        List<Topup> income = topupRepository.income(user,
            LocalDateTime.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth(), 00, 00, 00),
            LocalDateTime.of(endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth(), 23, 59, 59));
        Integer totalIncome = 0;
        for (Topup topup : income) {
            totalIncome += topup.getAmount();
        }

        IncomeExpensesResponse response = new IncomeExpensesResponse();
        response.setTotalIncome(totalIncome);
        response.setTotalExpenses(totalExpenses);
        return response;
    }
}
