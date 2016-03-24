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
                ws.data.jsonDal.JSONDAL.Update("app_master", "{'key_iid':'" + iid + "','appstatus':'" + appstatus + "'}", ref msg);
            }
            catch (Exception ex)
            {
                msg = ex.Message;
            }
        }

        /// <summary>
        /// 业主注册
        /// </summary>
        /// <param name="phone"></param>
        /// <param name="checkcode"></param>
        /// <param name="nick_name"></param>
        /// <param name="password"></param>
        /// <param name="roomid"></param>
        /// <param name="msg"></param>
        public void RegMaster(string phone, string checkcode, string nick_name, string password, string roomid, ref string msg)
        {
            msg = "";
        }


    }
}
