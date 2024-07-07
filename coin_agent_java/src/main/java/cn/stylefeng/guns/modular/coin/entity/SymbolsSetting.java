package cn.stylefeng.guns.modular.coin.entity;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.math.BigDecimal;

/**
  * <p> 交易对管理 实体类 </p>
 */
@Data
@TableName("coin_symbols_setting")
@Accessors(chain = true)
public class SymbolsSetting extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 交易对
      */
     @TableId(value="id", type= IdType.AUTO)
     private Long id;

     /**
      * 交易对
      */
     @TableField("symbol")
     private String symbol;

     /**
      * 星期几 弃用
      */
     @TableField("day")
     private String day;

     /**
      * 跌300 控制跌 生效 根据初始价格1000  一直跌 慢慢跌到 300 为止
      */
     @TableField("min_value")
     private BigDecimal minValue;

     /**
      *  涨1500 控制涨 生效 根据初始价格 1000 一直涨，慢慢涨到1500 为止
      */
     @TableField("max_value")
     private BigDecimal maxValue;

     /**
      * 自由浮动 10% 写10 自由浮动 生效 根据初始价格1000 在900到1100浮动
      *
      */
     @TableField("loss_ratio")
     private BigDecimal lossRatio;

     /**
      * 初始价格
      */
     @TableField("fist_open_price")
     private  BigDecimal fistOpenPrice;

//     初始价格 自由浮动生效 1000
//     高 自由浮动 第一次生效
//     开 自由浮动 第一次生效
//     低 自由浮动 第一次生效
//     收 自由浮动 第一次生效
//     涨 1500 控制涨 生效 根据初始价格 1000 一直涨，慢慢涨到1500 为止
//     跌 300 控制跌 生效 根据初始价格1000  一直跌 慢慢跌到 300 为止
//     自由浮动 10% 写10 自由浮动 生效 根据初始价格1000 在900到1100浮动


     //行情模式 模式分为 三种， 1、自由浮动 2、控制涨 3、控制跌
     private  BigDecimal p1LossRatio;
     //高 自由浮动 第一次生效
     private  BigDecimal p2LossRatio;
     //开
     private  BigDecimal p3LossRatio;
     //低
     private  BigDecimal p4LossRatio;
     //收
     private  BigDecimal p5LossRatio;

     //涨跌浮动
     private  BigDecimal p6LossRatio;
     //是否生成一个月数据
     private  BigDecimal p7LossRatio;
     private  BigDecimal p8LossRatio;
     private  BigDecimal p9LossRatio;
     private  BigDecimal p10LossRatio;
     private  BigDecimal p11LossRatio;
     private  BigDecimal p12LossRatio;
     private  BigDecimal p13LossRatio;
     private  BigDecimal p14LossRatio;
     private  BigDecimal p15LossRatio;
     private  BigDecimal p16LossRatio;
     private  BigDecimal p17LossRatio;
     private  BigDecimal p18LossRatio;
     private  BigDecimal p19LossRatio;
     private  BigDecimal p20LossRatio;
     private  BigDecimal p21LossRatio;
     private  BigDecimal p22LossRatio;
     private  BigDecimal p23LossRatio;
     private  BigDecimal p24LossRatio;
     private  BigDecimal p25LossRatio;
     private  BigDecimal p26LossRatio;
     private  BigDecimal p27LossRatio;
     private  BigDecimal p28LossRatio;
     private  BigDecimal p29LossRatio;
     private  BigDecimal p30LossRatio;
     private  BigDecimal p31LossRatio;
     private  BigDecimal p32LossRatio;
     private  BigDecimal p33LossRatio;
     private  BigDecimal p34LossRatio;
     private  BigDecimal p35LossRatio;
     private  BigDecimal p36LossRatio;
     private  BigDecimal p37LossRatio;
     private  BigDecimal p38LossRatio;
     private  BigDecimal p39LossRatio;
     private  BigDecimal p40LossRatio;
     private  BigDecimal p41LossRatio;
     private  BigDecimal p42LossRatio;
     private  BigDecimal p43LossRatio;
     private  BigDecimal p44LossRatio;
     private  BigDecimal p45LossRatio;
     private  BigDecimal p46LossRatio;
     private  BigDecimal p47LossRatio;
     private  BigDecimal p48LossRatio;
     private  BigDecimal p49LossRatio;
     private  BigDecimal p50LossRatio;
     private  BigDecimal p51LossRatio;
     private  BigDecimal p52LossRatio;
     private  BigDecimal p53LossRatio;
     private  BigDecimal p54LossRatio;
     private  BigDecimal p55LossRatio;
     private  BigDecimal p56LossRatio;
     private  BigDecimal p57LossRatio;
     private  BigDecimal p58LossRatio;
     private  BigDecimal p59LossRatio;
     private  BigDecimal p60LossRatio;
     private  BigDecimal p61LossRatio;
     private  BigDecimal p62LossRatio;
     private  BigDecimal p63LossRatio;
     private  BigDecimal p64LossRatio;
     private  BigDecimal p65LossRatio;
     private  BigDecimal p66LossRatio;
     private  BigDecimal p67LossRatio;
     private  BigDecimal p68LossRatio;
     private  BigDecimal p69LossRatio;
     private  BigDecimal p70LossRatio;
     private  BigDecimal p71LossRatio;
     private  BigDecimal p72LossRatio;
     private  BigDecimal p73LossRatio;
     private  BigDecimal p74LossRatio;
     private  BigDecimal p75LossRatio;
     private  BigDecimal p76LossRatio;
     private  BigDecimal p77LossRatio;
     private  BigDecimal p78LossRatio;
     private  BigDecimal p79LossRatio;
     private  BigDecimal p80LossRatio;
     private  BigDecimal p81LossRatio;
     private  BigDecimal p82LossRatio;
     private  BigDecimal p83LossRatio;
     private  BigDecimal p84LossRatio;
     private  BigDecimal p85LossRatio;
     private  BigDecimal p86LossRatio;
     private  BigDecimal p87LossRatio;
     private  BigDecimal p88LossRatio;
     private  BigDecimal p89LossRatio;
     private  BigDecimal p90LossRatio;
     private  BigDecimal p91LossRatio;
     private  BigDecimal p92LossRatio;
     private  BigDecimal p93LossRatio;
     private  BigDecimal p94LossRatio;
     private  BigDecimal p95LossRatio;
     private  BigDecimal p96LossRatio;
     private  BigDecimal p97LossRatio;
     private  BigDecimal p98LossRatio;
     private  BigDecimal p99LossRatio;
     private  BigDecimal p100LossRatio;
     private  BigDecimal p101LossRatio;
     private  BigDecimal p102LossRatio;
     private  BigDecimal p103LossRatio;
     private  BigDecimal p104LossRatio;
     private  BigDecimal p105LossRatio;
     private  BigDecimal p106LossRatio;
     private  BigDecimal p107LossRatio;
     private  BigDecimal p108LossRatio;
     private  BigDecimal p109LossRatio;
     private  BigDecimal p110LossRatio;
     private  BigDecimal p111LossRatio;
     private  BigDecimal p112LossRatio;
     private  BigDecimal p113LossRatio;
     private  BigDecimal p114LossRatio;
     private  BigDecimal p115LossRatio;
     private  BigDecimal p116LossRatio;
     private  BigDecimal p117LossRatio;
     private  BigDecimal p118LossRatio;
     private  BigDecimal p119LossRatio;
     private  BigDecimal p120LossRatio;
     private  BigDecimal p121LossRatio;
     private  BigDecimal p122LossRatio;
     private  BigDecimal p123LossRatio;
     private  BigDecimal p124LossRatio;
     private  BigDecimal p125LossRatio;
     private  BigDecimal p126LossRatio;
     private  BigDecimal p127LossRatio;
     private  BigDecimal p128LossRatio;
     private  BigDecimal p129LossRatio;
     private  BigDecimal p130LossRatio;
     private  BigDecimal p131LossRatio;
     private  BigDecimal p132LossRatio;
     private  BigDecimal p133LossRatio;
     private  BigDecimal p134LossRatio;
     private  BigDecimal p135LossRatio;
     private  BigDecimal p136LossRatio;
     private  BigDecimal p137LossRatio;
     private  BigDecimal p138LossRatio;
     private  BigDecimal p139LossRatio;
     private  BigDecimal p140LossRatio;
     private  BigDecimal p141LossRatio;
     private  BigDecimal p142LossRatio;
     private  BigDecimal p143LossRatio;
     private  BigDecimal p144LossRatio;
     private  BigDecimal p145LossRatio;
     private  BigDecimal p146LossRatio;
     private  BigDecimal p147LossRatio;
     private  BigDecimal p148LossRatio;
     private  BigDecimal p149LossRatio;
     private  BigDecimal p150LossRatio;
     private  BigDecimal p151LossRatio;
     private  BigDecimal p152LossRatio;
     private  BigDecimal p153LossRatio;
     private  BigDecimal p154LossRatio;
     private  BigDecimal p155LossRatio;
     private  BigDecimal p156LossRatio;
     private  BigDecimal p157LossRatio;
     private  BigDecimal p158LossRatio;
     private  BigDecimal p159LossRatio;
     private  BigDecimal p160LossRatio;
     private  BigDecimal p161LossRatio;
     private  BigDecimal p162LossRatio;
     private  BigDecimal p163LossRatio;
     private  BigDecimal p164LossRatio;
     private  BigDecimal p165LossRatio;
     private  BigDecimal p166LossRatio;
     private  BigDecimal p167LossRatio;
     private  BigDecimal p168LossRatio;
     private  BigDecimal p169LossRatio;
     private  BigDecimal p170LossRatio;
     private  BigDecimal p171LossRatio;
     private  BigDecimal p172LossRatio;
     private  BigDecimal p173LossRatio;
     private  BigDecimal p174LossRatio;
     private  BigDecimal p175LossRatio;
     private  BigDecimal p176LossRatio;
     private  BigDecimal p177LossRatio;
     private  BigDecimal p178LossRatio;
     private  BigDecimal p179LossRatio;
     private  BigDecimal p180LossRatio;
     private  BigDecimal p181LossRatio;
     private  BigDecimal p182LossRatio;
     private  BigDecimal p183LossRatio;
     private  BigDecimal p184LossRatio;
     private  BigDecimal p185LossRatio;
     private  BigDecimal p186LossRatio;
     private  BigDecimal p187LossRatio;
     private  BigDecimal p188LossRatio;
     private  BigDecimal p189LossRatio;
     private  BigDecimal p190LossRatio;
     private  BigDecimal p191LossRatio;
     private  BigDecimal p192LossRatio;
     private  BigDecimal p193LossRatio;
     private  BigDecimal p194LossRatio;
     private  BigDecimal p195LossRatio;
     private  BigDecimal p196LossRatio;
     private  BigDecimal p197LossRatio;
     private  BigDecimal p198LossRatio;
     private  BigDecimal p199LossRatio;
     private  BigDecimal p200LossRatio;
     private  BigDecimal p201LossRatio;
     private  BigDecimal p202LossRatio;
     private  BigDecimal p203LossRatio;
     private  BigDecimal p204LossRatio;
     private  BigDecimal p205LossRatio;
     private  BigDecimal p206LossRatio;
     private  BigDecimal p207LossRatio;
     private  BigDecimal p208LossRatio;
     private  BigDecimal p209LossRatio;
     private  BigDecimal p210LossRatio;
     private  BigDecimal p211LossRatio;
     private  BigDecimal p212LossRatio;
     private  BigDecimal p213LossRatio;
     private  BigDecimal p214LossRatio;
     private  BigDecimal p215LossRatio;
     private  BigDecimal p216LossRatio;
     private  BigDecimal p217LossRatio;
     private  BigDecimal p218LossRatio;
     private  BigDecimal p219LossRatio;
     private  BigDecimal p220LossRatio;
     private  BigDecimal p221LossRatio;
     private  BigDecimal p222LossRatio;
     private  BigDecimal p223LossRatio;
     private  BigDecimal p224LossRatio;
     private  BigDecimal p225LossRatio;
     private  BigDecimal p226LossRatio;
     private  BigDecimal p227LossRatio;
     private  BigDecimal p228LossRatio;
     private  BigDecimal p229LossRatio;
     private  BigDecimal p230LossRatio;
     private  BigDecimal p231LossRatio;
     private  BigDecimal p232LossRatio;
     private  BigDecimal p233LossRatio;
     private  BigDecimal p234LossRatio;
     private  BigDecimal p235LossRatio;
     private  BigDecimal p236LossRatio;
     private  BigDecimal p237LossRatio;
     private  BigDecimal p238LossRatio;
     private  BigDecimal p239LossRatio;
     private  BigDecimal p240LossRatio;
     private  BigDecimal p241LossRatio;
     private  BigDecimal p242LossRatio;
     private  BigDecimal p243LossRatio;
     private  BigDecimal p244LossRatio;
     private  BigDecimal p245LossRatio;
     private  BigDecimal p246LossRatio;
     private  BigDecimal p247LossRatio;
     private  BigDecimal p248LossRatio;
     private  BigDecimal p249LossRatio;
     private  BigDecimal p250LossRatio;
     private  BigDecimal p251LossRatio;
     private  BigDecimal p252LossRatio;
     private  BigDecimal p253LossRatio;
     private  BigDecimal p254LossRatio;
     private  BigDecimal p255LossRatio;
     private  BigDecimal p256LossRatio;
     private  BigDecimal p257LossRatio;
     private  BigDecimal p258LossRatio;
     private  BigDecimal p259LossRatio;
     private  BigDecimal p260LossRatio;
     private  BigDecimal p261LossRatio;
     private  BigDecimal p262LossRatio;
     private  BigDecimal p263LossRatio;
     private  BigDecimal p264LossRatio;
     private  BigDecimal p265LossRatio;
     private  BigDecimal p266LossRatio;
     private  BigDecimal p267LossRatio;
     private  BigDecimal p268LossRatio;
     private  BigDecimal p269LossRatio;
     private  BigDecimal p270LossRatio;
     private  BigDecimal p271LossRatio;
     private  BigDecimal p272LossRatio;
     private  BigDecimal p273LossRatio;
     private  BigDecimal p274LossRatio;
     private  BigDecimal p275LossRatio;
     private  BigDecimal p276LossRatio;
     private  BigDecimal p277LossRatio;
     private  BigDecimal p278LossRatio;
     private  BigDecimal p279LossRatio;
     private  BigDecimal p280LossRatio;
     private  BigDecimal p281LossRatio;
     private  BigDecimal p282LossRatio;
     private  BigDecimal p283LossRatio;
     private  BigDecimal p284LossRatio;
     private  BigDecimal p285LossRatio;
     private  BigDecimal p286LossRatio;
     private  BigDecimal p287LossRatio;
     private  BigDecimal p288LossRatio;


 }
