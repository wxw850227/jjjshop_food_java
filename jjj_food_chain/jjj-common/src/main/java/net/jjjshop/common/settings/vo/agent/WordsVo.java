package net.jjjshop.common.settings.vo.agent;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("自定义文字")
public class WordsVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Index index;
    private Apply apply;
    private Order order;
    private Team team;
    private CashList cashList;
    private CashApply cashApply;
    private Qrcode qrcode;

    @Data
    @Accessors(chain = true)
    @ApiModel("积分抵扣Vo")
    public static class Index implements Serializable {
        private static final long serialVersionUID = 1L;
        private String title;
        private String notAgent;
        private String applyNow;
        private String referee;
        private String money;
        private String freezeMoney;
        private String totalMoney;
        private String cash;

        public Index() {
            this.title = "分销中心";
            this.notAgent = "您还不是分销商，请先提交申请";
            this.applyNow = "立即加入";
            this.referee = "推荐人";
            this.money = "可提现佣金";
            this.freezeMoney = "待提现佣金";
            this.totalMoney = "已提现金额";
            this.cash = "去提现";
        }
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("申请成为分销商")
    public static class Apply implements Serializable {
        private static final long serialVersionUID = 1L;
        private String title;
        private String wordTitle;
        private String license;
        private String submit;
        private String waitAudit;
        private String gotoMall;

        public Apply() {
            this.title = "申请成为分销商";
            this.wordTitle = "请填写申请信息";
            this.license = "分销商申请协议";
            this.submit = "申请成为经销商";
            this.waitAudit = "您的申请已受理，正在进行信息核验，请耐心等待。";
            this.gotoMall = "去商城逛逛";
        }
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("分销订单")
    public static class Order implements Serializable {
        private static final long serialVersionUID = 1L;
        private String title;
        private String all;
        private String unsettled;
        private String settled;

        public Order() {
            this.title = "分销订单";
            this.all = "全部";
            this.unsettled = "未结算";
            this.settled = "已结算";
        }
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("我的团队")
    public static class Team implements Serializable {
        private static final long serialVersionUID = 1L;
        private String title;
        private String totalTeam;
        private String first;
        private String second;
        private String third;

        public Team() {
            this.title = "我的团队";
            this.totalTeam = "团队总人数";
            this.first = "一级团队";
            this.second = "二级团队";
            this.third = "三级团队";
        }
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("提现明细")
    public static class CashList implements Serializable {
        private static final long serialVersionUID = 1L;
        private String title;
        private String all;
        private String apply10;
        private String apply20;
        private String apply40;
        private String apply30;

        public CashList() {
            this.title = "提现明细";
            this.all = "全部";
            this.apply10 = "审核中";
            this.apply20 = "审核通过";
            this.apply40 = "已打款";
            this.apply30 = "驳回";
        }
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("申请提现")
    public static class CashApply implements Serializable {
        private static final long serialVersionUID = 1L;
        private String title;
        private String capital;
        private String money;
        private String moneyPlaceholder;
        private String minMoney;
        private String submit;

        public CashApply() {
            this.title = "申请提现";
            this.capital = "可提现佣金";
            this.money = "提现金额";
            this.moneyPlaceholder = "请输入要提取的金额";
            this.minMoney = "最低提现佣金";
            this.submit = "提交申请";
        }
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("推广二维码")
    public static class Qrcode implements Serializable {
        private static final long serialVersionUID = 1L;
        private String title;

        public Qrcode() {
            this.title = "推广二维码";
        }
    }

    public WordsVo() {
        this.index = new Index();
        this.apply = new Apply();
        this.order = new Order();
        this.team = new Team();
        this.cashList = new CashList();
        this.cashApply = new CashApply();
        this.qrcode = new Qrcode();
    }
}
