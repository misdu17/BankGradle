package com.syful.framework.web.platform.utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zakia on 1/12/17.
 */
public class QueryBuilder {
    private List<String> columns = new ArrayList<>();
    private List<String> tables = new ArrayList<>();
    private List<String> joins = new ArrayList<>();
    private List<String> leftJoins = new ArrayList<>();
    private List<String> wheres = new ArrayList<>();
    private List<String> orderBys = new ArrayList<>();
    private List<String> groupBys = new ArrayList<>();
    private List<String> havings = new ArrayList<>();

    public QueryBuilder(){}

    public QueryBuilder(String table) {
        tables.add(table);
    }

    private void appendList (StringBuilder sql, List<String> list, String init, String sep){
        boolean first = true;
        for (String s : list){
            if (first){
                sql.append(init);
            } else {
                sql.append(sep);
            }
            sql.append(s);
            first = false;
        }
    }

    public QueryBuilder column(String name) {
        columns.add(name);
        return this;
    }

    public QueryBuilder column(String name, boolean groupBy) {
        columns.add(name);
        if (groupBy) {
            groupBys.add(name);
        }
        return this;
    }

    public QueryBuilder from(String table) {
        tables.add(table);
        return this;
    }

    public QueryBuilder groupBy(String expr) {
        groupBys.add(expr);
        return this;
    }

    public QueryBuilder having(String expr) {
        havings.add(expr);
        return this;
    }

    public QueryBuilder join(String join) {
        joins.add(join);
        return this;
    }

    public QueryBuilder leftJoin(String join) {
        leftJoins.add(join);
        return this;
    }

    public QueryBuilder orderBy(String name) {
        orderBys.add(name);
        return this;
    }

    public QueryBuilder where(String expr) {
        wheres.add(expr);
        return this;
    }

    public String build() {
        StringBuilder sql = new StringBuilder("select ");

        if (columns.size() == 0) {
            sql.append("*");
        } else {
            appendList(sql, columns, "", ", ");
        }

        appendList(sql, tables, " from ", ", ");
        appendList(sql, joins, " join ", " join ");
        appendList(sql, leftJoins, " left join ", " left join ");
        appendList(sql, wheres, " where ", " and ");
        appendList(sql, groupBys, " group by ", ", ");
        appendList(sql, havings, " having ", " and ");
        appendList(sql, orderBys, " order by ", ", ");

        return sql.toString();
    }
}
