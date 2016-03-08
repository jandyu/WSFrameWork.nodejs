using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Collections;

namespace ws.data.jsonDal
{
    public class DALCache
    {
        const int CACHETIME = 1; //缓存有效期，秒

        static Hashtable cacheData = new Hashtable();

        public static bool haveCache (string key)
        {
            if(cacheData.ContainsKey(key)){
                Dictionary<string ,object> d= (Dictionary<string ,object>) cacheData[key]; 
                //缓存是否过期
                if (DateTime.Now.Ticks / 10000000 - long.Parse(d["dtm"].ToString()) < CACHETIME)
                {
                    return true;
                }
                else
                {
                    //过期删除
                    cacheData.Remove(key);
                }
            }
            return false;
        }

        public static string getCache(string key)
        {
            Dictionary<string, object> d = (Dictionary<string, object>)cacheData[key]; 
            return d["val"].ToString();
        }

        public static object getCacheObject(string key)
        {
            Dictionary<string, object> d = (Dictionary<string, object>)cacheData[key];
            return d["val"];
        }

        /// <summary>
        /// 设置缓存
        /// </summary>
        /// <param name="key"></param>
        /// <param name="val"></param>
        /// <param name="expire_dtm">缓存失效时间，单位：秒</param>
        public static void setCache(string key, object val, int expire_dtm)
        {
            //缓存，dtm为秒
            cacheData[key] = new Dictionary<string, object> { 
                {"val",val},{"dtm",(DateTime.Now.Ticks / 10000000 + expire_dtm).ToString()}
            };
            westsoft.data.xml.util.writeLog(key + " saved.");
        }

        public static void setCache(string key, object val)
        {
            setCache(key, val, 0);
        }

        public static void setCache(string key, string val)
        {
            setCache(key, (object)val);
        }


        public static string genKey(string str)
        {
            string key  = westsoft.data.xml.util.SHA1_Encrypt(str);
            westsoft.data.xml.util.writeLog(key + ":" + str);
            return key;

        }


    }

    public class DALCacheDict
    {
        const string CACHENAME_DICT = "SYSCACHE_V_DICT";

        const int CACHE_TIME = 60*60;

        public static void refeshData(){
            PageData p = JSONDAL.Query("v_dict", "[]", 1, 99999, "");
            DALCache.setCache(CACHENAME_DICT, p, CACHE_TIME);
        }

        public static PageData getDict(){
            if (!DALCache.haveCache(CACHENAME_DICT))
            {
                refeshData();
            }
            return (PageData)DALCache.getCacheObject(CACHENAME_DICT);
        }
    }
}
