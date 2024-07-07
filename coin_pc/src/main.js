import 'babel-polyfill'
import promise from 'es6-promise'
promise.polyfill()
import 'url-search-params-polyfill'
import Vue from 'vue'
import i18n from './i18n'

import App from './App'
import router from './router'
import VueCountryIntl from 'vue-country-intl';
import 'vue-country-intl/lib/vue-country-intl.css'
import less from 'less'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css'
import 'element-ui/lib/index.js'
import '../static/ico/iconfont.js'//引入阿里云开发者图标
import '../static/ico/iconfont.css'//引入阿里云开发者图标
import '../static/zmzit/zmcss.css'//引入第三方字体
import webSocket from '@/js/websocket.js';
import './js/jquery-3.2.1.min.js'
var momentTimezone = require('moment-timezone');
import "./style/theme/day.css"
import "./style/theme/night.css"

// 全局注册组件
Vue.component(VueCountryIntl.name, VueCountryIntl);
Vue.config.productionTip = true
Vue.use(less)
Vue.use(ElementUI)
Vue.prototype.getTimezone4K = function () {
  var curlang = i18n.locale;
  if (curlang == "en") {
    return "America/Los_Angeles";
  }
  if (curlang == "jp") {
    return "Asia/Tokyo";
  }
  if (curlang == "ko") {
    return "Asia/Seoul";
  }
  if (curlang == "fay") { //德国
    return "Europe/Berlin";
  }
  if (curlang == "fr_FR") {
    return "Europe/Paris";
  }
  if (curlang == "it_IT") {
    return "Europe/Rome";
  }
  if (curlang == "es_ES") {
    return "Europe/Madrid";
  }
  if (curlang == "tc") {
    return "Asia/Hong_Kong";
  }
  if (curlang == "vn_VN") {
    return "Asia/Ho_Chi_Minh";
  }
  if (curlang == "zh") {
    return "Asia/Shanghai";
  }
  return curlang;
};
Vue.prototype.dateFormatHM = function (tick) {
  return momentTimezone(tick).tz(this.getTimezone4K()).format("YYYY-MM-DD HH:mm:ss");
};
import VueBus from 'vue-bus';
Vue.use(VueBus);
import JwChat from 'jwchat';

/* 在 0.2.041 之前的版本需要引入 css */
Vue.use(JwChat)
Vue.prototype.$webSocket = webSocket;

new Vue({
  el: '#app',
  i18n,
  router,
  components: { App },
  template: '<App/>'
})
