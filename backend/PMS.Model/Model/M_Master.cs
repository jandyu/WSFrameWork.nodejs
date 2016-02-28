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
    public class M_Master: IModel
    {
        public M_Master()
        {
        }

        /// <summary>
        /// 修改app状态
        /// </summary>
        /// <param name="iid"></param>
        /// <param name="appstatus"></param>
        /// <param name="msg"></param>
        public static void UpdAppStatus(string iid, string appstatus, ref string msg)
        {
            try
            {
                ws.data.jsonDal.JSONDAL.Update("sys_master", "{'key_iid':'" + iid + "','appstatus':'" + appstatus + "'}", ref msg);
            }
            catch (Exception ex)
            {
                msg = ex.Message;
            }
        }

    }
}
