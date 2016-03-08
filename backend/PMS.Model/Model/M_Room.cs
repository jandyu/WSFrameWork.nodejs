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
    public class M_Room: IModel
    {
        public M_Room()
        {
        }

        /// <summary>
        /// 修改挂牌或者撤销
        /// </summary>
        /// <param name="iid"></param>
        /// <param name="flag"></param>
        /// <param name="msg"></param>
        public static void UpdSaleFlag(string iid, string flag, ref string msg)
        {
            try
            {
                ws.data.jsonDal.JSONDAL.Update("app_wy_room", "{'key_iid':'" + iid + "','saleflag':'" + flag + "'}", ref msg);
            }
            catch (Exception ex)
            {
                msg = ex.Message;
            }
        }


        public static void UpdLeaseFlag(string iid, string flag, ref string msg)
        {
            try
            {
                ws.data.jsonDal.JSONDAL.Update("app_wy_room", "{'key_iid':'" + iid + "','leaseflag':'" + flag + "'}", ref msg);
            }
            catch (Exception ex)
            {
                msg = ex.Message;
            }
        }
    }
}
