using System;
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
    public class JSTools
    {
        /// <summary>
        /// 根据json字符串创建DataTable
        /// </summary>
        /// <param name="strJson">[{""rid"":""1"",""module_id"":""mispos"",""role_id"":""purchase6"",""role_name"":""采购6"",""role_data"":""purchase"",""memo"":"""",""iid"":""16"",""ROWSTAT"":""""},{""rid"":""2"",""module_id"":""mispos"",""role_id"":""purchase6"",""role_name"":""采购6"",""role_data"":""purchase"",""memo"":"""",""iid"":""17"",""ROWSTAT"":""""}]</param>
        /// <returns></returns>
        public static DataTable CreateDataTableByJson(string strJson)
        {
            #region
            DataTable dt = new DataTable();
            dt.TableName = "Table";
            JArray jarry = JArray.Parse(strJson);
            for (int i = 0; i < jarry.Count; i++)
            {
                //1、如果是第一行数据,则创建表的结构
                if (i == 0)
                {
                    foreach (var obj in JObject.Parse(jarry[0].ToString()))
                    {
                        dt.Columns.Add(obj.Key.ToString());
                    }
                }
                //2、添加行数据
                DataRow dr = dt.NewRow();
                foreach (var obj in JObject.Parse(jarry[i].ToString()))
                {
                    dr[obj.Key.ToString()] = obj.Value.ToString();
                }
                dt.Rows.Add(dr);

            }
            #endregion
            return dt;
        }

        /// <summary>
        /// 根据json字符串转换成a=aaa&b=bbb&c=ddd
        /// </summary>
        /// <param name="strJson">{a:"aaa",b:"bbb"}</param>
        /// <returns></returns>
        public static string GetPostParamByJson(string strJson)
        {
            StringBuilder sb = new StringBuilder();
            foreach (var obj in JObject.Parse(strJson))
            {
                //dr[obj.Key.ToString()] = obj.Value.ToString();
                sb.AppendFormat("{0}={1}&", obj.Key.ToString(), obj.Value.ToString());
            }
            if (sb.Length > 0)
            {
                sb.Remove(sb.Length - 1, 1);
            }
            return sb.ToString();
        }

    }
}
