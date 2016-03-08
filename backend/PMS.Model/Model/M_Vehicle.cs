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
    public class M_Vehicle: IModel
    {
        public M_Vehicle()
        {
        }

        /// <summary>
        /// 修改挂牌或者撤销
        /// </summary>
        /// <param name="iid"></param>
        /// <param name="flag"></param>
        /// <param name="msg"></param>
        public static void UpdStatus(string iid, string flag, ref string msg)
        {
            try
            {
                ws.data.jsonDal.JSONDAL.Update("app_wy_vehicle", "{'key_iid':'" + iid + "','status':'" + flag + "'}", ref msg);
            }
            catch (Exception ex)
            {
                msg = ex.Message;
            }
        }

    }
}
