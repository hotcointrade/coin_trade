<template>
  <!--交易币种行情-->
  <vue-seamless-scroll :data="marketList" :class-option="optionLeft" class="warp">

    <ul class="transaction_pair">
      <li v-for="(item, index) in marketList" :key="item.symbol + index" @click="detailFun(item)">
        <div class="pairList">
          <div growing-ignore="true" class="trade-block">
            <ul class="market-ticker">
              <li data-ha-click="">
                <a>
                  <dl class="htusdt">
                    <dt>
                      <strong><img :src="item.img"
                          style="width: 20px;height: 20px;margin-right: 4px;border-radius: 30px;position: relative;top: -2px;" />{{
    item.symbol }}</strong>
                    </dt>
                    <dd class="price">
                      ${{ Number(item.close).toFixed(2) }}
                      <em>≈ {{ Number(item.close * rateNumber).toFixed(2) }} </em>
                    </dd>
                    <dd class="vol">24H Vol {{ item.amount }}</dd>
                    <dd class="rate rise">
                      <p class="loss" v-if="Number(item.rose) > 0">+{{ Number(item.rose).toFixed(2) }}%
                        <img src="../assets/lt.png" style="position: absolute;top: 68px;right: -2px;width: 100%;" />
                      </p>
                      <p class="profie" v-else>{{ Number(item.rose).toFixed(2) }}%
                        <img src="../assets/ht.png" style="position: absolute;top: 68px;right: -2px;width: 100%;" />
                      </p>
                    </dd>

                  </dl>
                </a>
              </li>
            </ul>
          </div>
        </div>
      </li>
    </ul>
  </vue-seamless-scroll>
</template>
<script>
import { ticketApi } from "@/api/getData";
import vueSeamlessScroll from "vue-seamless-scroll";
export default {
  props: {
    marketList: {
      type: Array,
      required: true
    },
  },
  components: {
    vueSeamlessScroll,
  },

  data() {
    return {
      // marketList: [], //币种行情数据
      timer: null,
      rateNumber: sessionStorage.getItem("platFormRateNumber"),
    };
  },
  computed: {
    optionLeft() {
      return {
        direction: 2,
        limitMoveNum: 2,
        hoverStop: false,
      };
    },
  },
  created() {
    // this.ticketFun();
  },
  mounted() {
    // that.timer = setInterval(() => {
    //   that.ticketFun();
    // }, 2000);
  },
  methods: {
    detailFun(item) {
      this.$router.push('/currencyTrade?symbol=' + item.symbol);
    },
    // async ticketFun() {
    //   //币种行情
    //   var data = new URLSearchParams();
    //   data.set("type", this.activeName);
    //   var res = await ticketApi(data);
    //   this.marketList = [];
    //   var arr = res.data;
    //   arr.forEach((element) => {
    //     if (element.symbol != "" && element.rose !== null) {
    //       this.marketList.push(element);
    //     }
    //   });
    //   // this.startSubscription()
    // },

    // //开始订阅
    // startSubscription() {
    // 	this.$webSocket.addDoOn("/ticket", (session, message) => {
    // 		var response = JSON.parse(message.dataAsString())
    // 		// 列表数据
    // 		var index = this.marketList.findIndex(item => {
    // 			return item.symbol == response.symbol
    // 		})
    // 		if (index !=-1){
    // 			this.marketList.splice(index, 1, JSON.parse(response.data))
    // 		}
    // 	})
    // },

  },
  destroyed() {
  },
};
</script>
<style lang="less">
.warp {
  overflow: hidden;
}

.transaction_pair {
  display: -ms-flexbox;
  display: flex;
  -ms-flex-pack: center;
  justify-content: center;

  border-top: 1px solid hsla(0, 0%, 100%, 0.15);

  color: #536ea5;

  .pairList {
    width: 240px;
  }

  .profie {
    color: #da1e1e;

  }

  .loss {


    color: #0da88b;
  }
}

dd {
  display: block;
  margin-inline-start: 0px;
}
</style>
