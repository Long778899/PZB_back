package com.peizhenbao.modules.dashboard.controller;

import com.peizhenbao.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "后台-控制台首页", description = "后台首页数据统计接口")
@RestController
@RequestMapping("/api/console/dashboard")
@RequiredArgsConstructor
public class ConsoleDashboardController {

    private final JdbcTemplate jdbcTemplate;

    @Operation(summary = "获取全平台基础数据统计", description = "用于后台首页顶部卡片展示当前收录的医院数、覆盖省份数、医生数等")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> data = new HashMap<>();

        // 查询覆盖省份数量
        Long provinceCount = jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT province) FROM hospitals", Long.class);
        data.put("provinceCount", provinceCount == null ? 0 : provinceCount);

        // 查询医院数量
        Long hospitalCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM hospitals", Long.class);
        data.put("hospitalCount", hospitalCount == null ? 0 : hospitalCount);

        // 查询科室数量
        Long departmentCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM departments", Long.class);
        data.put("departmentCount", departmentCount == null ? 0 : departmentCount);

        // 查询医生数量
        Long doctorCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM doctors", Long.class);
        data.put("doctorCount", doctorCount == null ? 0 : doctorCount);

        // 查询注册用户数量
        Long userCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Long.class);
        data.put("userCount", userCount == null ? 0 : userCount);

        return Result.success(data);
    }
}
