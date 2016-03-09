using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;
using System.Data;
using SN.Data;
namespace MSGSrv
{
    /// <summary>
    /// DealMsg 的摘要说明
    /// </summary>
    //[ScriptService]                             //令WebService成功传入Json参数，并以Json形式返回结果
    //[GenerateScriptType(typeof(Person))]        //不是必要，但推荐添加(如果Person里面再嵌套另一个复杂类型，则必要声明)

    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // 若要允许使用 ASP.NET AJAX 从脚本中调用此 Web 服务，请取消注释以下行。 
    [System.Web.Script.Services.ScriptService]
    public class DealMsg : System.Web.Services.WebService
    {

        [WebMethod]
        public string HelloWorld()
        {

            DataTable dt=DBUtility.DB.FromSql("select * from app_push_noti_template ").ToDataTable();
            return "Hello World";
        }

        [WebMethod]
        public Models.app_resource_message DealMessage(String iid)
        {

            var arm=DBUtility.GetSingle<Models.app_resource_message>(Models.app_resource_message._.iid == iid);

            return arm;
        }
    }
}
