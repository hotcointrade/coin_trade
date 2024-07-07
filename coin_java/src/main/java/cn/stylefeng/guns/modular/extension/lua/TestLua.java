package cn.stylefeng.guns.modular.extension.lua;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;



public class TestLua
{
    public static void main(String[] args)
    {

        String luacmd="print('hello world!')";
        Globals globals=JsePlatform.standardGlobals();
        //LuaValue对象用来表示在Lua语言的基本数据类型，比如:Nil,Number,String,Table,userdata,Function等
        LuaValue chunk= globals.load(luacmd);
        chunk.call();

        /**
         * 脚本文件执行
         */
        String luaPath="F:\\xunzi\\RCC\\src\\main\\java\\cn\\stylefeng\\guns\\extension\\lua\\script\\login.lua";
        //加载文件 ,编译
        globals.loadfile(luaPath).call();
        LuaValue func=globals.get(LuaValue.valueOf("hello"));
        func.call(LuaValue.valueOf("tom"));

    }
}
