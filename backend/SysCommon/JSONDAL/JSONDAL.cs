using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Newtonsoft.Json.Linq;
using System.Data;

namespace ws.data.jsonDal
{

    public class JSONDAL
    {
        /// <summary>
        /// 
        /// </summary>
        public static string str_dbDefault = "";//PMS.Sys.CSystem.baseSystem.str_dbDefault

        /// <summary>
        /// 查询
        /// </summary>
        /// <param name="defid"></param>
        /// <param name="fmtid"></param>
        /// <param name="strparam">[{'col':'字段名称','logic':'=','val':'字段值','andor':'and'},{'col':'字段名称','logic':'!=','val':'字段值','andor':''}]</param>
        /// <param name="dStyle"></param>
        /// <param name="currpage"></param>
        /// <param name="pagesize"></param>
        /// <param name="order">排序{'col':'aid','sort':'desc'}或者[{'col':'aid','sort':'desc'},{'col':'name','sort':'asc'}]</param>
        /// <returns></returns>
        public static PageData Query(string defid, string fmtid, string strparam, string dStyle = "json", int currpage = 1, int pagesize = 9999, string order = "")
        {
            try
            {
                if (strparam == "") strparam = "[]";
                if (order == "") order = "[]";
                if (order.Substring(0, 1) != "[")
                {
                    order = "[" + order + "]";
                }

                string order_key = "";

                if (defid.IndexOf('.') > -1)
                {
                    order_key = defid.Split('.')[1] + "_table";
                }
                else
                {
                    order_key = defid;
                }
                //{'common':{'currpage':1,'pagesize':9999,'where':[{'col':'字段名称','logic':'=','val':'字段值','andor':'and'},{'col':'字段名称','logic':'!=','val':'字段值','andor':''}]}}
                string pstda = string.Format("{{ 'defid': '{0}', 'fmtid': '{1}', 'strparam':\"{{'common':{{'currpage':{4},'pagesize':{5},'where':{2}}},'{7}':{{'order':{6}}}}}\", 'dStyle': '{3}' }}", defid, fmtid, strparam, dStyle, currpage, pagesize, order,order_key);

                string rtn = "";

                //-----------------------------------------------------------------------------
                //缓存处理
                bool haveCache = false;
                string cacheKey = DALCache.genKey(SendHttpRequest.strUrl + pstda);
                if (DALCache.haveCache(cacheKey))
                {
                    rtn = DALCache.getCache(cacheKey);
                    haveCache = true;
                }
                //无缓存
                if (!haveCache)
                {
                    rtn = SendHttpRequest.SendRequestByJson(pstda);
                    DALCache.setCache(cacheKey, rtn);
                }
                //===============================================================================
                

                if (rtn.Length < 5)
                {
                    throw new Exception("请检查参数是否正确！");
                }

                rtn = rtn.Substring(4);
                rtn = rtn.Substring(0, rtn.Length - 1);

                PageData pd = new PageData();
                JObject obj = JObject.Parse(rtn);
                //defid = tableanme + "_table";
                //包含有.符号的就是没有def配置文件的直接从表中读取数据
                if (defid.IndexOf('.') > -1)
                {
                    defid=defid.Split('.')[1]+ "_table";
                }
                pd.i_currpage = int.Parse(obj["data"][0][defid]["currpage"].ToString());
                pd.i_pagesize = int.Parse(obj["data"][0][defid]["pagesize"].ToString());
                pd.i_totalpages = int.Parse(obj["data"][0][defid]["totalpages"].ToString());
                pd.i_totalrows = int.Parse(obj["data"][0][defid]["totalrows"].ToString());
                if (pd.i_totalrows > 0)
                {
                    pd.dt_data = JSTools.CreateDataTableByJson(obj["data"][0][defid]["d"].ToString());
                }

                return pd;
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }


        /// <summary>
        /// 查询数据
        /// </summary>
        /// <param name="database">数据库</param>
        /// <param name="tableanme">表名</param>
        /// <param name="strparam">"[{'col':'字段名称','logic':'=','val':'字段值','andor':'and'},{'col':'字段名称','logic':'!=','val':'字段值','andor':''}]"</param>        
        /// <param name="currpage"></param>
        /// <param name="pagesize"></param>
        /// <param name="order">排序{'col':'aid','sort':'desc'}或者[{'col':'aid','sort':'desc'},{'col':'name','sort':'asc'}]</param>
        /// <returns></returns>  
        public static PageData Query(string database, string tableanme, string strparam,int currpage=1,int pagesize=9999,string order="")
        {
            try
            {
                if (strparam == "") strparam = "[]";
                if (order == "") order = "[]";
                if (order.Substring(0, 1) != "[")
                {
                    order = "[" + order + "]";
                }
                
                //string pstda = string.Format("{{ 'defid': '{0}.{1}', 'fmtid': 'json', 'strparam':\"{{'common':{{'currpage':{3},'pagesize':{4},'where':{2}}},'{1}_table':{{'order':{5}}} }}\", 'dStyle': 'json' }}", database, tableanme, strparam, currpage, pagesize,order);

                string defid=string.Format("{0}.{1}",database,tableanme);
                string fmtid="json";
                return Query(defid, fmtid, strparam, "json", currpage, pagesize, order);
                /*
                string rtn = "";
                rtn = SendHttpRequest.SendRequestByJson(pstda);

                if (rtn.Length < 5)
                {
                    throw new Exception("请检查参数是否正确！");
                }

                rtn = rtn.Substring(4);
                rtn = rtn.Substring(0, rtn.Length - 1);

                PageData pd = new PageData();
                JObject obj = JObject.Parse(rtn);
                string defid = tableanme.ToLower() + "_table";
                pd.i_currpage = int.Parse(obj["data"][0][defid]["currpage"].ToString());
                pd.i_pagesize = int.Parse(obj["data"][0][defid]["pagesize"].ToString());
                pd.i_totalpages = int.Parse(obj["data"][0][defid]["totalpages"].ToString());
                pd.i_totalrows = int.Parse(obj["data"][0][defid]["totalrows"].ToString());
                if (pd.i_totalrows > 0)
                {
                    pd.dt_data = JSTools.CreateDataTableByJson(obj["data"][0][defid]["d"].ToString());
                }
                

                return pd;
                * */
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }

        /// <summary>
        /// 查询
        /// </summary>
        /// <param name="tableanme">表名，例如:sys_employee</param>
        /// <param name="strparam">"[{'col':'字段名称','logic':'=','val':'字段值','andor':'and'},{'col':'字段名称','logic':'!=','val':'字段值','andor':''}]"</param>
        /// <param name="currpage">显示第几页</param>
        /// <param name="pagesize">每页多少条数据</param>
        /// <param name="order">排序{'col':'aid','sort':'desc'}或者[{'col':'aid','sort':'desc'},{'col':'name','sort':'asc'}]</param>
        /// <returns></returns>
        public static PageData Query(string tableanme, string strparam,int currpage=1,int pagesize=9999,string order="")
        {
            string database = str_dbDefault;
            if (database == "") database = "db_drsyn";
            return Query(database, tableanme, strparam,currpage,pagesize,order);
        }

        /// <summary>
        /// 添加
        /// </summary>
        /// <param name="tablename">表名，例如:sys_employee</param>
        /// <param name="strJson">{a:'aaa',b:'bb'}或[{a:'aaa',b:'bb'},{a:'aaa1',b:'bb2'}]</param>
        /// <msg></msg>
        /// <returns>添加成功的id,例如:100或1,2,3</returns>
        public static string Insert(string tablename,string strJson,ref string msg)
        {
            try
            {
                string database = str_dbDefault;
                if (database == "") database = "db_drsyn";
                tablename = database + "." + tablename;
                ws.data.jsonDal.OperateDataJsonHelper opdata = new ws.data.jsonDal.OperateDataJsonHelper();
                opdata.Gen_Insert(tablename, strJson, ref msg);
                string pstda = opdata.GenOperateJson();
                string rtn = SendHttpRequest.SendRequestByJson(pstda);
                if (rtn.Substring(0, 6) == "<error")
                {
                    msg = rtn;
                    return "";
                }                
                return rtn.Replace("<succ msg='", "").Replace("'/>", "");
            }
            catch (Exception ex)
            {
                msg = ex.Message;
                return "";
            }
        }

        /// <summary>
        /// 修改
        /// </summary>
        /// <param name="tablename">表名，例如:sys_employee</param>
        /// <param name="strJson">{a:'aaa',b:'bb',key_iid:1}或[{a:'aaa',b:'bb',key_iid:1},{a:'aaa1',b:'bb2',key_iid:2}]</param>
        /// <param name="msg"></param>
        public static void Update(string tablename, string strJson, ref string msg)
        {
            try
            {
                string database = str_dbDefault;
                if (database == "") database = "db_drsyn";
                tablename = database + "." + tablename;

                ws.data.jsonDal.OperateDataJsonHelper opdata = new ws.data.jsonDal.OperateDataJsonHelper();
                opdata.Gen_Update(tablename, strJson, ref msg);
                string pstda = opdata.GenOperateJson();
                string rtn = SendHttpRequest.SendRequestByJson(pstda);
                if (rtn.Substring(0, 6) == "<error")
                {
                    msg = rtn;
                }
            }
            catch (Exception ex)
            {
                msg = ex.Message;
            }
        }



        /// <summary>
        /// 删除
        /// </summary>
        /// <param name="tablename">表名，例如:sys_employee</param>
        /// <param name="strJson">["10","11"]或{"key1":"201201010001","key2":"201201010001"}或者{"where_1":"input_dt>='20120101'"}</param>
        /// <param name="msg"></param>
        public static void Delete(string tablename, string strJson, ref string msg)
        {
            try
            {
                string database = str_dbDefault;
                if (database == "") database = "db_drsyn";
                tablename = database + "." + tablename;

                ws.data.jsonDal.OperateDataJsonHelper opdata = new ws.data.jsonDal.OperateDataJsonHelper();
                opdata.Gen_Delete(tablename, strJson, ref msg);
                string pstda = opdata.GenOperateJson();
                string rtn = SendHttpRequest.SendRequestByJson(pstda);
                if (rtn.Substring(0, 6) == "<error")
                {
                    msg = rtn;
                }
            }
            catch (Exception ex)
            {
                msg = ex.Message;
            }
        }

        
        /// <summary>
        /// 开始事物
        /// </summary>
        public static ws.data.jsonDal.OperateDataJsonHelper beginTran()
        {
            return new ws.data.jsonDal.OperateDataJsonHelper();            
        }

        /// <summary>
        /// 添加
        /// </summary>
        /// <param name="tablename">表名，例如:sys_employee</param>
        /// <param name="strJson">{a:'aaa',b:'bb'}或[{a:'aaa',b:'bb'},{a:'aaa1',b:'bb2'}]</param>
        /// <param name="tran"></param>
        /// <param name="msg"></param>
        public static void tranInsert(string tablename, string strJson, ws.data.jsonDal.OperateDataJsonHelper tran,ref string msg)
        {
            try
            {
                string database = str_dbDefault;
                if (database == "") database = "db_drsyn";
                tablename = database + "." + tablename;                
                tran.Gen_Insert(tablename, strJson, ref msg);               
            }
            catch (Exception ex)
            {
                msg = ex.Message;
            }
        }



        /// <summary>
        /// 修改
        /// </summary>
        /// <param name="tablename">表名，例如:sys_employee</param>
        /// <param name="strJson">{a:'aaa',b:'bb',key_iid:1}或[{a:'aaa',b:'bb',key_iid:1},{a:'aaa1',b:'bb2',key_iid:2}]</param>
        /// <param name="tran"></param>
        /// <param name="msg"></param>
        public static void tranUpdate(string tablename, string strJson, ws.data.jsonDal.OperateDataJsonHelper tran, ref string msg)
        {
            try
            {
                string database = str_dbDefault;
                if (database == "") database = "db_drsyn";
                tablename = database + "." + tablename;                
                tran.Gen_Update(tablename, strJson, ref msg);             
            }
            catch (Exception ex)
            {
                msg = ex.Message;
            }
        }


        /// <summary>
        /// 删除
        /// </summary>
        /// <param name="tablename">表名，例如:sys_employee</param>
        /// <param name="strJson">["10","11"]或{"key1":"201201010001","key2":"201201010001"}或者{"where_1":"input_dt>='20120101'"}</param>
        /// <param name="tran"></param>
        /// <param name="msg"></param>
        public static void tranDelete(string tablename, string strJson, ws.data.jsonDal.OperateDataJsonHelper tran, ref string msg)
        {
            try
            {
                string database = str_dbDefault;
                if (database == "") database = "db_drsyn";
                tablename = database + "." + tablename;                
                tran.Gen_Delete(tablename, strJson, ref msg);
                
            }
            catch (Exception ex)
            {
                msg = ex.Message;
            }
        }

        /// <summary>
        /// 提交
        /// </summary>
        /// <param name="tran"></param>
        public static void commit(ws.data.jsonDal.OperateDataJsonHelper tran,ref string msg)
        {
            try
            {
                string pstda = tran.GenOperateJson();
                string rtn = SendHttpRequest.SendRequestByJson(pstda);
                if (rtn.Substring(0, 6) == "<error")
                {
                    msg = rtn;
                }
            }
            catch (Exception ex)
            {
                msg = ex.Message;
            } 
        }                
    }  
   
}
