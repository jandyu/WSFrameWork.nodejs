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
    public class M_User:IModel
    {
        public M_User()
        {
        }


        /// <summary>
        /// 添加员工
        /// </summary>
        /// <param name="userid"></param>
        /// <param name="nickname"></param>
        /// <param name="fullname"></param>
        /// <param name="passwd"></param>
        /// <param name="depid"></param>
        /// <param name="roleid"></param>
        /// <param name="status"></param>
        /// <param name="empid"></param>
        /// <param name="iid"></param>
        /// <param name="sex"></param>
        /// <param name="telephone"></param>
        /// <param name="address"></param>
        /// <param name="email"></param>
        /// <param name="duty"></param>
        /// <param name="birthday"></param>
        /// <param name="msg"></param>
        public static void AddUser(string iid, string userid, string nickname, string fullname, string passwd, string depid, string roleid, string status, string empid, string sex, string telephone, string address, string email, string duty, string birthday, ref string msg)
        {
            try
            {
                ws.data.jsonDal.JSONDAL.Insert("sys_user", "{'userid':'" + userid + "','nickname':'" + nickname + "','fullname':'" + fullname + "','passwd':'" + passwd + "','depid':'" + depid + "','roleid':'" + roleid + "','status':'" + status + "','empid':'" + empid + "','sex':'" + sex + "','telephone':'" + telephone + "','address':'" + address + "','email':'" + email + "','duty':'" + duty + "','birthday':'" + birthday + "'}", ref msg);
            }
            catch (Exception ex)
            {
                msg = ex.Message;
            }
        }

        /// <summary>
        /// 修改员工信息
        /// </summary>
        /// <param name="iid"></param>
        /// <param name="userid"></param>
        /// <param name="nickname"></param>
        /// <param name="fullname"></param>
        /// <param name="passwd"></param>
        /// <param name="depid"></param>
        /// <param name="roleid"></param>
        /// <param name="status"></param>
        /// <param name="empid"></param>
        /// <param name="sex"></param>
        /// <param name="telephone"></param>
        /// <param name="address"></param>
        /// <param name="email"></param>
        /// <param name="duty"></param>
        /// <param name="birthday"></param>
        /// <param name="msg"></param>
        public static void UpdateUser(string iid, string userid, string nickname, string fullname, string passwd, string depid, string roleid, string status, string empid, string sex, string telephone, string address, string email, string duty, string birthday, ref string msg)
        {
            try
            {
                ws.data.jsonDal.JSONDAL.Update("sys_user", "{'key_iid':'" + iid + "','userid':'" + userid + "','nickname':'" + nickname + "','fullname':'" + fullname + "','passwd':'" + passwd + "','depid':'" + depid + "','roleid':'" + roleid + "','status':'" + status + "','empid':'" + empid + "','sex':'" + sex + "','telephone':'" + telephone + "','address':'" + address + "','email':'" + email + "','duty':'" + duty + "','birthday':'" + birthday + "'}", ref msg);
            }
            catch (Exception ex)
            {
                msg = ex.Message;
            }
        }

        public static void DeleteUser(string iid,ref string msg)
        {
            try
            {
                    ws.data.jsonDal.JSONDAL.Delete("sys_user", "{'iid':'" + iid.ToString() + "'}", ref msg);                
            }
            catch (Exception ex)
            {
                msg = ex.Message;
            }
        }
    }
}
