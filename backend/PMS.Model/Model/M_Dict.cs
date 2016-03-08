using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using ws.data.jsonDal;
using System.Data;
namespace PMS.Model
{
    /// <summary>
    /// 建筑物结构
    /// </summary>
    public class M_Dict:IModel
    {
        public M_Dict()
        {
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        public static PageData getDictTree(string category)
        {
            StringBuilder sb = new StringBuilder();
            sb.Append("[");

            //p_id
            if (category != "")
            {
                sb.AppendFormat("{{'col':'category','logic':'=','val':'{0}','andor':'and'}},", category);
            }


            //结束
            sb.Append("{'col':'1','logic':'=','val':'1','andor':''}]");

            return ws.data.jsonDal.JSONDAL.Query("sys_dict",sb.ToString(), 1, 9999, "[{'col':'iid','sort':'asc'}]");
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="p_id"></param>
        /// <returns></returns>
        public static PageData getDictByPid(int parent_id)
        {
            return ws.data.jsonDal.JSONDAL.Query("sys_dict", "[{'col':'parent_id','logic':'=','val':'" + parent_id.ToString() + "','andor':''}]", 1, 9999, "[{'col':'iid','sort':'asc'}]");
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        public static PageData getDictByCateAndPID(string category,string pid="")
        {
            /*
            StringBuilder sb = new StringBuilder();
            sb.Append("[");

            //p_id
            if (category != "")
            {
                sb.AppendFormat("{{'col':'category','logic':'=','val':'{0}','andor':'and'}},", category);
            }
            //p_id
            if (pid != "")
            {
                sb.AppendFormat("{{'col':'pid','logic':'=','val':'{0}','andor':'and'}},", pid);
            }


            //结束
            sb.Append("{'col':'1','logic':'=','val':'1','andor':''}]");

            return ws.data.jsonDal.JSONDAL.Query("v_dict", sb.ToString(), 1, 9999, "[{'col':'iid','sort':'asc'}]");
             * */
            return getDict(category, pid, "");
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        public static PageData getDictByCateAndPIDAndId(string category, string pid = "",string id="")
        {
            /*
            StringBuilder sb = new StringBuilder();
            sb.Append("[");

            //p_id
            if (category != "")
            {
                sb.AppendFormat("{{'col':'category','logic':'=','val':'{0}','andor':'and'}},", category);
            }
            //p_id
            if (pid != "")
            {
                sb.AppendFormat("{{'col':'pid','logic':'=','val':'{0}','andor':'and'}},", pid);
            }

            //id
            if (id != "")
            {
                sb.AppendFormat("{{'col':'id','logic':'=','val':'{0}','andor':'and'}},", id);
            }



            //结束
            sb.Append("{'col':'1','logic':'=','val':'1','andor':''}]");

            return ws.data.jsonDal.JSONDAL.Query("v_dict", sb.ToString(), 1, 9999, "[{'col':'iid','sort':'asc'}]");
             * */
            return getDict(category, pid, id);
        }

        public static PageData getDict(string category,string pid,string id)
        {
            PageData pd=new PageData();

            PageData pd1 = DALCacheDict.getDict();

            DataView dv = pd1.dt_data.Copy().DefaultView;            
            string filter = "";
            if (category != "")
            {
                filter = "category='" + category + "'";
            }

            if (pid != "")
            {
                filter = filter + " and pid='" + pid + "'";
            }

            if (id != "")
            {
                filter = filter + " and id='" + id + "'";
            }

            dv.RowFilter = filter;

            pd.dt_data = dv.ToTable();
            if (pd.dt_data.Rows.Count == 0)
            {
                pd.dt_data = null;
            }
            return pd;
        }


        /// <summary>
        /// 添加部门
        /// </summary>
        /// <param name="parent_id"></param>
        /// <param name="Dict_name"></param>
        /// <param name="order_id"></param>
        /// <param name="memo"></param>
        /// <param name="msg"></param>
        public static void AddDict(string parent_id,string item_id,string order_id,string title,string category,ref string msg)
        {
            try
            {
                ws.data.jsonDal.JSONDAL.Insert("sys_dict", "{'parent_id':'" + parent_id.ToString() + "','item_id':'" + item_id + "','order_id':'" + order_id + "','title':'" + title + "','category':'" + category + "'}", ref msg);
            }
            catch (Exception ex)
            {
                msg = ex.Message;
            }
        }

        /// <summary>
        /// 修改部门
        /// </summary>
        /// <param name="iid"></param>
        /// <param name="p_id"></param>
        /// <param name="Dict_name"></param>
        /// <param name="order_id"></param>
        /// <param name="memo"></param>
        /// <param name="msg"></param>
        public static void UpdateDict(int iid,string parent_id, string item_id, string order_id, string title, string category, ref string msg)
        {
            try
            {
                ws.data.jsonDal.JSONDAL.Update("sys_dict", "{'key_iid':'" + iid.ToString() + "','parent_id':'" + parent_id.ToString() + "','item_id':'" + item_id + "','order_id':'" + order_id + "','title':'" + title + "','category':'" + category + "'}", ref msg);
            }
            catch (Exception ex)
            {
                msg = ex.Message;
            }
        }

        public static void DeleteDict(int iid,ref string msg)
        {
            try
            {
                if (getDictByPid(iid).dt_data != null)
                {
                    msg = "请首先删除下级设置!";
                    return;
                }
                else
                {
                    ws.data.jsonDal.JSONDAL.Delete("sys_dict", "{'iid':'" + iid.ToString() + "'}", ref msg);
                }
            }
            catch (Exception ex)
            {
                msg = ex.Message;
            }
        }


    }
}
