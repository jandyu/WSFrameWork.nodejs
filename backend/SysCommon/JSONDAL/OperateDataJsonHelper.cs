using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Data;
using Newtonsoft.Json.Linq;
namespace ws.data.jsonDal
{   
    /// <summary>
    /// json处理类
    /// </summary>
    public class OperateDataJsonHelper
    {
        /// <summary>
        /// 任意已经存在的defxml文件名用于拼装defid
        /// </summary>
        public string defid = PMS.Sys.CSystem.baseSystem.str_dbDefault;//并没有真实的用到
        //string pstda = "{ defid: '{0}', fmtid: 'update', strparam: {1}, dStyle: 'xml' };";

        /// <summary>
        /// 后台的defxml数据配置文件，如果是自动生成的则用db_test.sys_employee【数据库定义xml.表名】
        /// </summary>
        public ArrayList tblList=new ArrayList();//重要

        public Dictionary<string, JObject> dic_data = new Dictionary<string, JObject>();

        public OperateDataJsonHelper()
        {
            if (defid == "")
            {
                defid = "db_drsyn";
            }
        }

       
        /// <summary>
        /// 生成insert、update、delete Json数据
        /// </summary>
        /// <param name="xmldef"></param>
        /// <param name="strUpdJson">添加或者修改的json{a:'aaa',b:'bb'}或者[{a:'aaa',b:'bb'},{a:'aaa1',b:'bb2'}]；修改时是{a:'aaa',b:'bb',key_iid:'1'}或[{a:'aaa',b:'bb'},{a:'aaa1',b:'bb2',key_iid:'2'}]</param>
        /// <param name="strDelJson">删除的json["10","11"]或{"bill_code":"201201010001"}或者{"where_1":"input_dt>='20120101'"}</param>
        /// <param name="flag">类型:insert、update、delete</param>
        public void Gen_Data(string xmldef,string strUpdJson,string flag,string strDelJson)
        {
            #region 
            
            try
            {
                //参数初始化
                string c0 = "8";
                if (flag.ToLower() == "insert")
                {
                    c0 = "8";
                }
                if (flag.ToLower() == "update")
                {
                    c0 = "9";
                }

                //1、判断是否存在
                if (tblList.Contains(xmldef) == false)
                {
                    dic_data.Add(xmldef, JObject.Parse("{}"));
                    tblList.Add(xmldef);
                }
                //2、取出表对应的原有Json数据
                //"db_test.sys_employee":
                //{ data: [{ c0: "8", name: $("#name").val() }], //新增
                //    delData: []
                //}
                JObject obj_xmldef = dic_data[xmldef];

                //3、取出obj_xmldef的data部分            
                JArray arr_data = JArray.Parse((obj_xmldef["data"] ?? "[]").ToString());

                //4、添加行记录到数组中
                if (strUpdJson.Length > 0)
                {
                    if (strUpdJson.Substring(0, 1) == "{")//如果只有一条记录
                    {
                        JObject jobj_one = JObject.Parse(strUpdJson);                        
                        jobj_one.AddFirst(new JProperty("c0", c0));
                        //jobj_one["c0"] = c0;
                        /*
                        if (c0 == "9")//修改                       
                        {
                            string iid = (jobj_one["iid"] ?? "").ToString();
                            if (iid == "")
                            {
                                throw new Exception("iid 不能为空!");
                            }
                            jobj_one.Remove("iid");
                            jobj_one["key_iid"] = iid;
                        }*/
                        arr_data.Add(jobj_one);
                    }
                    else
                    {
                        JArray jarr_muti = JArray.Parse(strUpdJson);
                        for (int i = 0; i < jarr_muti.Count; i++)
                        {
                            JObject jobj_one = JObject.Parse(jarr_muti[i].ToString());
                            jobj_one.AddFirst(new JProperty("c0", c0));
                            //jobj_one["c0"] = c0;
                            /*
                            if (c0 == "9")//修改                                               
                            {
                                string iid = (jobj_one["iid"] ?? "").ToString();
                                if (iid == "")
                                {
                                    throw new Exception("iid 不能为空!");
                                }
                                jobj_one.Remove("iid");
                                jobj_one["key_iid"] = iid;
                            }*/
                            arr_data.Add(jobj_one);
                        }
                    }
                }

                //5、将处理后的数组赋值给原有数据
                obj_xmldef["data"] = arr_data;                
                //6、处理删除数据                
                if (strDelJson.Length > 0)
                {
                    if (strDelJson.Substring(0, 1) == "[")//数组
                    {
                        obj_xmldef["delData"] = JArray.Parse(strDelJson);   
                    }
                    if (strDelJson.Substring(0, 1) == "{")//数组
                    {
                        obj_xmldef["delData"] = JObject.Parse(strDelJson);   
                    }
                }

            }
            catch (Exception ex)
            {
                throw ex;
            }
            #endregion
        }


        /// <summary>
        /// 生成insert json
        /// </summary>
        /// <param name="xmldef"></param>
        /// <param name="json">{a:'aaa',b:'bb'}或者[{a:'aaa',b:'bb'},{a:'aaa1',b:'bb2'}]</param>
        /// <param name="msg"></param>
        public void Gen_Insert(string xmldef,string json,ref string msg)
        {
            try
            {
                Gen_Data(xmldef, json, "insert", "");
            }
            catch(Exception ex)
            {
                msg = ex.Message;
            }
        }
        /// <summary>
        /// 生成update json
        /// </summary>
        /// <param name="xmldef"></param>
        /// <param name="json">{'key_iid':1,a:'aaa',b:'bb'}或者[{'key_iid':1,a:'aaa',b:'bb'},{'key_iid':2,a:'aaa1',b:'bb2'}]</param>
        /// <param name="msg"></param>
        public void Gen_Update(string xmldef, string json, ref string msg)
        {
            try
            {
                Gen_Data(xmldef, json, "update", "");
            }
            catch (Exception ex)
            {
                msg = ex.Message;
            }
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="xmldef"></param>
        /// <param name="json">["10","11"]或{"bill_code":"201201010001"}或者{"where_1":"input_dt>='20120101'"}</param>
        /// <param name="msg"></param>
        public void Gen_Delete(string xmldef, string json, ref string msg)
        {
            try
            {
                Gen_Data(xmldef, "", "delete", json);
            }
            catch (Exception ex)
            {
                msg = ex.Message;
            }
        }

        /// <summary>
        /// 生成最终的{ defid: '{0}', fmtid: 'update', strparam: '....', dStyle: 'xml' }
        /// </summary>
        /// <returns></returns>
        public string GenOperateJson()
        {            
            StringBuilder sb = new StringBuilder();
            sb.Append("{ \"tblList\": [");
            foreach (string tb in tblList)
            {
                sb.AppendFormat("\"{0}\",",tb);
            }

            sb.Remove(sb.Length - 1, 1);//移除最后一个符号","
            sb.Append("],");
            foreach (string tb in tblList)
            {
                sb.AppendFormat("\"{0}\": ",tb);
                sb.Append(dic_data[tb].ToString());
                sb.Append(",");
            }
            sb.Remove(sb.Length - 1, 1);//移除最后一个符号","
                        
            sb.Append("}");

            string pstda = string.Format("{{ \"defid\": \"{0}\", \"fmtid\": \"update\", \"strparam\":'{1}', \"dStyle\": \"xml\" }}", this.defid,sb.ToString());

            return pstda;
        }
    }
}
