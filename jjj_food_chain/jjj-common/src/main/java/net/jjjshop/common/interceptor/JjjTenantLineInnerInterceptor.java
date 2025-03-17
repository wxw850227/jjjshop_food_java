package net.jjjshop.common.interceptor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.parser.SqlParserHelper;
import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
import com.baomidou.mybatisplus.core.toolkit.ClassUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils.MPBoundSql;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils.MPStatementHandler;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.toolkit.PropertyMapper;
import net.jjjshop.config.constant.CommonRedisKey;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.core.bean.RequestDetail;
import net.jjjshop.framework.core.util.RequestDetailThreadLocal;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.NotExpression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.data.redis.core.RedisTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class JjjTenantLineInnerInterceptor extends JsqlParserSupport implements InnerInterceptor {
    private TenantLineHandler tenantLineHandler;
    // 是否带appid，通过设置comment实现
    private boolean notWithAppId = false;
    // 当前环境
    private String env;
    // redis
    private RedisTemplate redisTemplate;
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        this.notWithAppId = false;
        //获取参数
        if(boundSql.getParameterObject() instanceof Map){
            Map<String, Object> map = (HashMap<String, Object>)boundSql.getParameterObject();
            for(Map.Entry<String, Object> entry : map.entrySet()){
                if(entry.getValue() instanceof LambdaQueryWrapper) {
                    LambdaQueryWrapper mapValue = (LambdaQueryWrapper) entry.getValue();
                    if ("/*notWithAppId*/".equalsIgnoreCase(mapValue.getSqlComment())) {
                        this.notWithAppId = true;
                        break;
                    }
                }
            }
        }


        if (InterceptorIgnoreHelper.willIgnoreTenantLine(ms.getId())
                || Long.parseLong(this.tenantLineHandler.getTenantId().toString()) == 0
                || this.notWithAppId) {
            return;
        }

        if (!SqlParserHelper.getSqlParserInfo(ms)) {
            MPBoundSql mpBs =  PluginUtils.mpBoundSql(boundSql);
            mpBs.sql(this.parserSingle(mpBs.sql(), (Object)null));
        }
    }

    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        MPStatementHandler mpSh = PluginUtils.mpStatementHandler(sh);
        MappedStatement ms = mpSh.mappedStatement();
        SqlCommandType sct = ms.getSqlCommandType();
        if (sct == SqlCommandType.INSERT || sct == SqlCommandType.UPDATE || sct == SqlCommandType.DELETE) {
            // 如果是uat环境，不准操作delete
            if(env.equals("uat")){
                RequestDetail requestDetail = RequestDetailThreadLocal.getRequestDetail();
                // 有可能是定时任务
                if(requestDetail != null){
                    String ip = (String)redisTemplate.opsForValue().get(CommonRedisKey.getRedisKey(CommonRedisKey.UAT_IP));
                    if(sct == SqlCommandType.UPDATE || sct == SqlCommandType.DELETE){
                        if(requestDetail.getPath().startsWith("/api/shop/")){
                            // 如果是admin，并且是白名单可以操作
                            if (requestDetail.getUserName().equals("admin") && !requestDetail.getIp().equals(ip)) {
                                throw new BusinessException("演示环境，数据不允许删除");
                            }
                            if (requestDetail.getUserName().equals("test") && requestDetail.getPath().equals("/api/shop/passport/editPass")) {
                                throw new BusinessException("演示环境，test账户不允许修改密码");
                            }
                        }if(requestDetail.getPath().startsWith("/api/supplier/")){
                            // 如果是admin，并且是白名单可以操作
                            if (requestDetail.getUserName().equals("admin") && !requestDetail.getIp().equals(ip)) {
                                throw new BusinessException("演示环境，数据不允许删除");
                            }
                            if (requestDetail.getUserName().equals("test") && requestDetail.getPath().equals("/api/supplier/passport/editPass")) {
                                throw new BusinessException("演示环境，test账户不允许修改密码");
                            }
                        }else if(requestDetail.getPath().startsWith("/api/admin/")){
                            if (!requestDetail.getIp().equals(ip)) {
                                throw new BusinessException("演示环境，数据不允许修改");
                            }
                        }
                    }
                }
            }

            if (InterceptorIgnoreHelper.willIgnoreTenantLine(ms.getId())
                    || Long.parseLong(this.tenantLineHandler.getTenantId().toString()) == 0) {
                return;
            }

            if (SqlParserHelper.getSqlParserInfo(ms)) {
                return;
            }

            MPBoundSql mpBs = mpSh.mPBoundSql();
            mpBs.sql(this.parserMulti(mpBs.sql(), (Object)null));
        }

    }

    protected void processSelect(Select select, int index, String sql, Object obj) {
        this.processSelectBody(select.getSelectBody());
        List<WithItem> withItemsList = select.getWithItemsList();
        if (!CollectionUtils.isEmpty(withItemsList)) {
            withItemsList.forEach(this::processSelectBody);
        }

    }

    protected void processSelectBody(SelectBody selectBody) {
        if (selectBody != null) {
            if (selectBody instanceof PlainSelect) {
                this.processPlainSelect((PlainSelect)selectBody);
            } else if (selectBody instanceof WithItem) {
                WithItem withItem = (WithItem)selectBody;
                this.processSelectBody(withItem.getSelectBody());
            } else {
                SetOperationList operationList = (SetOperationList)selectBody;
                if (operationList.getSelects() != null && operationList.getSelects().size() > 0) {
                    operationList.getSelects().forEach(this::processSelectBody);
                }
            }

        }
    }

    protected void processInsert(Insert insert, int index, String sql, Object obj) {
        if (!this.tenantLineHandler.ignoreTable(insert.getTable().getName())) {
            List<Column> columns = insert.getColumns();
            if (!CollectionUtils.isEmpty(columns)) {
                String tenantIdColumn = this.tenantLineHandler.getTenantIdColumn();
                if (!columns.stream().map(Column::getColumnName).anyMatch((i) -> {
                    return i.equals(tenantIdColumn);
                })) {
                    columns.add(new Column(this.tenantLineHandler.getTenantIdColumn()));
                    Select select = insert.getSelect();
                    if (select != null) {
                        this.processInsertSelect(select.getSelectBody());
                    } else {
                        if (insert.getItemsList() == null) {
                            throw ExceptionUtils.mpe("Failed to process multiple-table update, please exclude the tableName or statementId", new Object[0]);
                        }

                        ItemsList itemsList = insert.getItemsList();
                        if (itemsList instanceof MultiExpressionList) {
                            ((MultiExpressionList)itemsList).getExprList().forEach((el) -> {
                                el.getExpressions().add(this.tenantLineHandler.getTenantId());
                            });
                        } else {
                            ((ExpressionList)itemsList).getExpressions().add(this.tenantLineHandler.getTenantId());
                        }
                    }

                }
            }
        }
    }

    protected void processUpdate(Update update, int index, String sql, Object obj) {
        Table table = update.getTable();
        if (!this.tenantLineHandler.ignoreTable(table.getName())) {
            update.setWhere(this.andExpression(table, update.getWhere()));
        }
    }

    protected void processDelete(Delete delete, int index, String sql, Object obj) {
        if (!this.tenantLineHandler.ignoreTable(delete.getTable().getName())) {
            delete.setWhere(this.andExpression(delete.getTable(), delete.getWhere()));
        }
    }

    protected BinaryExpression andExpression(Table table, Expression where) {
        EqualsTo equalsTo = new EqualsTo();
        equalsTo.setLeftExpression(this.getAliasColumn(table));
        equalsTo.setRightExpression(this.tenantLineHandler.getTenantId());
        if (null != where) {
            return where instanceof OrExpression ? new AndExpression(equalsTo, new Parenthesis(where)) : new AndExpression(equalsTo, where);
        } else {
            return equalsTo;
        }
    }

    protected void processInsertSelect(SelectBody selectBody) {
        PlainSelect plainSelect = (PlainSelect)selectBody;
        FromItem fromItem = plainSelect.getFromItem();
        if (fromItem instanceof Table) {
            Table fromTable = (Table)fromItem;
            plainSelect.setWhere(this.builderExpression(plainSelect.getWhere(), fromTable));
            this.appendSelectItem(plainSelect.getSelectItems());
        } else if (fromItem instanceof SubSelect) {
            SubSelect subSelect = (SubSelect)fromItem;
            this.appendSelectItem(plainSelect.getSelectItems());
            this.processInsertSelect(subSelect.getSelectBody());
        }

    }

    protected void appendSelectItem(List<SelectItem> selectItems) {
        if (!CollectionUtils.isEmpty(selectItems)) {
            if (selectItems.size() == 1) {
                SelectItem item = (SelectItem)selectItems.get(0);
                if (item instanceof AllColumns || item instanceof AllTableColumns) {
                    return;
                }
            }

            selectItems.add(new SelectExpressionItem(new Column(this.tenantLineHandler.getTenantIdColumn())));
        }
    }

    protected void processPlainSelect(PlainSelect plainSelect) {
        FromItem fromItem = plainSelect.getFromItem();
        Expression where = plainSelect.getWhere();
        this.processWhereSubSelect(where);
        if (fromItem instanceof Table) {
            Table fromTable = (Table)fromItem;
            if (!this.tenantLineHandler.ignoreTable(fromTable.getName())) {
                plainSelect.setWhere(this.builderExpression(where, fromTable));
            }
        } else {
            this.processFromItem(fromItem);
        }

        List<Join> joins = plainSelect.getJoins();
        if (joins != null && joins.size() > 0) {
            joins.forEach((j) -> {
                this.processJoin(j);
                this.processFromItem(j.getRightItem());
            });
        }

    }

    protected void processWhereSubSelect(Expression where) {
        if (where != null) {
            if (where instanceof FromItem) {
                this.processFromItem((FromItem)where);
            } else {
                if (where.toString().indexOf("SELECT") > 0) {
                    if (where instanceof BinaryExpression) {
                        BinaryExpression expression = (BinaryExpression)where;
                        this.processWhereSubSelect(expression.getLeftExpression());
                        this.processWhereSubSelect(expression.getRightExpression());
                    } else if (where instanceof InExpression) {
                        InExpression expression = (InExpression)where;
                        ItemsList itemsList = expression.getRightItemsList();
                        if (itemsList instanceof SubSelect) {
                            this.processSelectBody(((SubSelect)itemsList).getSelectBody());
                        }
                    } else if (where instanceof ExistsExpression) {
                        ExistsExpression expression = (ExistsExpression)where;
                        this.processWhereSubSelect(expression.getRightExpression());
                    } else if (where instanceof NotExpression) {
                        NotExpression expression = (NotExpression)where;
                        this.processWhereSubSelect(expression.getExpression());
                    } else if (where instanceof Parenthesis) {
                        Parenthesis expression = (Parenthesis)where;
                        this.processWhereSubSelect(expression.getExpression());
                    }
                }

            }
        }
    }

    protected void processFromItem(FromItem fromItem) {
        if (fromItem instanceof SubJoin) {
            SubJoin subJoin = (SubJoin)fromItem;
            if (subJoin.getJoinList() != null) {
                subJoin.getJoinList().forEach(this::processJoin);
            }

            if (subJoin.getLeft() != null) {
                this.processFromItem(subJoin.getLeft());
            }
        } else if (fromItem instanceof SubSelect) {
            SubSelect subSelect = (SubSelect)fromItem;
            if (subSelect.getSelectBody() != null) {
                this.processSelectBody(subSelect.getSelectBody());
            }
        } else if (fromItem instanceof ValuesList) {
            this.logger.debug("Perform a subquery, if you do not give us feedback");
        } else if (fromItem instanceof LateralSubSelect) {
            LateralSubSelect lateralSubSelect = (LateralSubSelect)fromItem;
            if (lateralSubSelect.getSubSelect() != null) {
                SubSelect subSelect = lateralSubSelect.getSubSelect();
                if (subSelect.getSelectBody() != null) {
                    this.processSelectBody(subSelect.getSelectBody());
                }
            }
        }

    }

    protected void processJoin(Join join) {
        if (join.getRightItem() instanceof Table) {
            Table fromTable = (Table)join.getRightItem();
            if (this.tenantLineHandler.ignoreTable(fromTable.getName())) {
                return;
            }

            join.setOnExpression(this.builderExpression(join.getOnExpression(), fromTable));
        }

    }

    protected Expression builderExpression(Expression currentExpression, Table table) {
        EqualsTo equalsTo = new EqualsTo();
        equalsTo.setLeftExpression(this.getAliasColumn(table));
        equalsTo.setRightExpression(this.tenantLineHandler.getTenantId());
        if (currentExpression == null) {
            return equalsTo;
        } else {
            return currentExpression instanceof OrExpression ? new AndExpression(new Parenthesis(currentExpression), equalsTo) : new AndExpression(currentExpression, equalsTo);
        }
    }

    protected Column getAliasColumn(Table table) {
        StringBuilder column = new StringBuilder();
        if (table.getAlias() != null) {
            column.append(table.getAlias().getName()).append(".");
        }

        column.append(this.tenantLineHandler.getTenantIdColumn());
        return new Column(column.toString());
    }

    public void setProperties(Properties properties) {
        PropertyMapper.newInstance(properties).whenNotBlack("tenantLineHandler", ClassUtils::newInstance, this::setTenantLineHandler);
    }

    public TenantLineHandler getTenantLineHandler() {
        return this.tenantLineHandler;
    }

    public void setTenantLineHandler(final TenantLineHandler tenantLineHandler) {
        this.tenantLineHandler = tenantLineHandler;
    }

    public JjjTenantLineInnerInterceptor() {
    }

    public JjjTenantLineInnerInterceptor(final TenantLineHandler tenantLineHandler, String env, RedisTemplate redisTemplate) {
        this.tenantLineHandler = tenantLineHandler;
        this.env = env;
        this.redisTemplate = redisTemplate;
    }

    public String toString() {
        return "JjjTenantLineInnerInterceptor(super=" + super.toString() + ", tenantLineHandler=" + this.getTenantLineHandler() + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof net.jjjshop.common.interceptor.JjjTenantLineInnerInterceptor)) {
            return false;
        } else {
            net.jjjshop.common.interceptor.JjjTenantLineInnerInterceptor other = (net.jjjshop.common.interceptor.JjjTenantLineInnerInterceptor)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (!super.equals(o)) {
                return false;
            } else {
                Object this$tenantLineHandler = this.getTenantLineHandler();
                Object other$tenantLineHandler = other.getTenantLineHandler();
                if (this$tenantLineHandler == null) {
                    if (other$tenantLineHandler != null) {
                        return false;
                    }
                } else if (!this$tenantLineHandler.equals(other$tenantLineHandler)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof net.jjjshop.common.interceptor.JjjTenantLineInnerInterceptor;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = super.hashCode();
        Object $tenantLineHandler = this.getTenantLineHandler();
        result = result * 59 + ($tenantLineHandler == null ? 43 : $tenantLineHandler.hashCode());
        return result;
    }
}