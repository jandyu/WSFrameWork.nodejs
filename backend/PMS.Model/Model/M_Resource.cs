﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using ws.data.jsonDal;
namespace PMS.Model//ws.data.jsonDal
{
    /// <summary>
    /// 建筑物结构
    /// </summary>
    public class M_Resource:IModel
    {
        public M_Resource()
        {
        }

      
        /// <summary>
        /// 
        /// </summary>
        /// <param name="cate">类型</param>
        /// <param name="objid">资源所属对象id</param>
        /// <returns></returns>
        public static PageData GetResource(string cate,string objid)
        {
            return ws.data.jsonDal.JSONDAL.Query("app_wy_resource", "[{'col':'category','logic':'=','val':'" + cate + "','andor':' and '},{'col':'objid','logic':'=','val':'" + objid + "','andor':''}]", 1, 9999, "[{'col':'iid','sort':'asc'}]");
        }

        /// <summary>
        /// 添加资源信息
        /// </summary>
        /// <param name="p_id"></param>
        /// <param name="dept_name"></param>
        /// <param name="order_id"></param>
        /// <param name="memo"></param>
        /// <param name="msg"></param>
        public static void AddResource(string category, string objid, string url, string memo, ref string msg)
        {
            try
            {
                ws.data.jsonDal.JSONDAL.Insert("app_wy_resource", "{'category':'" + category + "','objid':'" + objid + "','url':'" + url + "','memo':'" + memo + "'}", ref msg);
            }
            catch (Exception ex)
            {
                msg = ex.Message;
            }
        }


        public static void UpdateResource(string iid,string category,string objid,string url,string memo,ref string msg)
        {
            try
            {
                ws.data.jsonDal.JSONDAL.Update("app_wy_resource", "{'key_iid':'" + iid + "','category':'" + category + "','objid':'" + objid + "','url':'" + url + "','memo':'" + memo + "'}", ref msg);
            }
            catch (Exception ex)
            {
                msg = ex.Message;
            }
        }

        /// <summary>
        /// 删除
        /// </summary>
        /// <param name="tablename"></param>
        /// <param name="iids"></param>
        /// <param name="msg"></param>
        public static void DelResource(string tablename, string iids, ref string msg)
        {
            ws.data.jsonDal.JSONDAL.Delete(tablename, iids, ref msg);
        }



    }
}
