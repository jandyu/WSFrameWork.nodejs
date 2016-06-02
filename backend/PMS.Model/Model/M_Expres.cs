using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using ws.data.jsonDal;
namespace PMS.Model
{
    /// <summary>
    /// 员工管理
    /// </summary>
    public class M_Expres:IModel
    {
        public M_Expres()
        {
        }

        
        /// <summary>
        /// 获取快递统计信息
        /// </summary>
        /// <returns></returns>
        public static PageData getTJXX()
        {
            return ws.data.jsonDal.JSONDAL.Query("v_express_tj", "[]", 1, 9999, "[{'col':'iid','sort':'asc'}]");
        }

        /// <summary>
        /// 修改状态
        /// </summary>
        /// <param name="iid"></param>
        /// <param name="appstatus"></param>
        /// <param name="msg"></param>
        public static void ReceiveExpres(string iid, string status,string user, ref string msg)
        {
            try
            {
                ws.data.jsonDal.JSONDAL.Update("app_wy_expres", "{'key_iid':'" + iid + "','status':'" + status + "','receiver':'" + user + "','receive_dt':'"+System.DateTime.Now.ToString("yyyy-MM-dd")+"'}", ref msg);
            }
            catch (Exception ex)
            {
                msg = ex.Message;
            }
        }
    }
}
