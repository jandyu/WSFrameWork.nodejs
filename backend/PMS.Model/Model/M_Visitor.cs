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
    public class M_Visitor:IModel
    {
        public M_Visitor()
        {
        }

        
        

        /// <summary>
        /// 修改状态
        /// </summary>
        /// <param name="iid"></param>
        /// <param name="appstatus"></param>
        /// <param name="msg"></param>
        public static void ReceiveVisitor(string iid, string status, string user, ref string msg)
        {
            try
            {
                ws.data.jsonDal.JSONDAL.Update("app_wy_visitor", "{'key_iid':'" + iid + "','status':'" + status + "','receiver':'" + user + "','dtm_real':'"+System.DateTime.Now.ToString("yyyy-MM-dd")+"'}", ref msg);
            }
            catch (Exception ex)
            {
                msg = ex.Message;
            }
        }

    }
}
