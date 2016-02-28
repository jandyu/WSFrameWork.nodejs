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
    public class M_Park: IModel
    {
        public M_Park()
        {
        }

        /// <summary>
        /// 修改出租状态
        /// </summary>
        /// <param name="iid"></param>
        /// <param name="flag"></param>
        /// <param name="msg"></param>
        public static void UpdLeaseStatus(string iid, string flag, ref string msg)
        {
            try
            {
                ws.data.jsonDal.JSONDAL.Update("app_wy_park", "{'key_iid':'" + iid + "','leasestatus':'" + flag + "'}", ref msg);
            }
            catch (Exception ex)
            {
                msg = ex.Message;
            }
        }

    }
}
