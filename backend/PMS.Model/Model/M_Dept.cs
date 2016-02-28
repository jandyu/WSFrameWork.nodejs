using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using ws.data.jsonDal;
namespace PMS.Model
{
    /// <summary>
    /// 建筑物结构
    /// </summary>
    public class M_Dept:IModel
    {
        public M_Dept()
        {
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        public static PageData getDeptTree()
        {
            return ws.data.jsonDal.JSONDAL.Query("sys_dept", "[{'col':'iid','logic':'>','val':'0','andor':''}]", 1, 9999, "[{'col':'iid','sort':'asc'}]");
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="p_id"></param>
        /// <returns></returns>
        public static PageData getDeptByPid(int p_id)
        {
            return ws.data.jsonDal.JSONDAL.Query("sys_dept", "[{'col':'p_id','logic':'=','val':'"+p_id.ToString()+"','andor':''}]", 1, 9999, "[{'col':'iid','sort':'asc'}]");
        }

        /// <summary>
        /// 添加部门
        /// </summary>
        /// <param name="p_id"></param>
        /// <param name="dept_name"></param>
        /// <param name="order_id"></param>
        /// <param name="memo"></param>
        /// <param name="msg"></param>
        public static void AddDept(int p_id,string dept_name,string order_id,string memo,ref string msg)
        {
            try
            {
                ws.data.jsonDal.JSONDAL.Insert("sys_dept", "{'p_id':'" + p_id.ToString() + "','dept_name':'" + dept_name + "','order_id':'" + order_id+ "','memo':'" + memo + "'}", ref msg);
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
        /// <param name="dept_name"></param>
        /// <param name="order_id"></param>
        /// <param name="memo"></param>
        /// <param name="msg"></param>
        public static void UpdateDept(int iid, int p_id, string dept_name, string order_id, string memo, ref string msg)
        {
            try
            {
                ws.data.jsonDal.JSONDAL.Update("sys_dept", "{'key_iid':'"+iid.ToString()+"','p_id':'" + p_id.ToString() + "','dept_name':'" + dept_name + "','order_id':'" + order_id + "','memo':'" + memo + "'}", ref msg);
            }
            catch (Exception ex)
            {
                msg = ex.Message;
            }
        }

        public static void DeleteDept(int iid,ref string msg)
        {
            try
            {
                if (getDeptByPid(iid).dt_data != null)
                {
                    msg = "请首先删除下级部门!";
                    return;
                }
                else
                {
                    ws.data.jsonDal.JSONDAL.Delete("sys_dept", "{'iid':'" + iid.ToString() + "'}", ref msg);
                }
            }
            catch (Exception ex)
            {
                msg = ex.Message;
            }
        }
    }
}
