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
        /// <summary>
        /// 客户端访问方法
        /// $.getJSON("http://localhost:50045/DealMsg.asmx/DealMessage?jsoncallback=?",{iid:349},function(rtn){console.info(rtn);})
        /// </summary>
        /// <param name="iid"></param>
        [WebMethod]
        public void DealMessage()
        {           
            string msg = "";//
            string state = "succ";//succ表示成功
            DealMessageReal(ref msg);
            string jsoncallback = HttpContext.Current.Request["jsoncallback"];
            string result = jsoncallback + "({state:'"+state+"',msg:'"+msg+"'})";
            HttpContext.Current.Response.Write(result);
            HttpContext.Current.Response.End();
        }

        public void DealMessageReal(ref string msg)
        {
            msg = "";
            using (SN.Data.DbTrans tran = DBUtility.DB.BeginTrans())
            {
                try
                {
                    int tm_stamp = GetTimestampFromDatetime(System.DateTime.Now);
                    var lst = tran.From<Models.app_resource_message>().Where(Models.app_resource_message._.status == "0" && Models.app_resource_message._.reserve_time<=tm_stamp).
                        OrderBy(new OrderByClip("priority desc,iid asc")).ToList<Models.app_resource_message>();
                    for (int i = 0; i < lst.Count(); i++)
                    {
                        var arm = lst[i];
                       
                            int targettype = arm.Target_type ?? 0;

                            //处理相应的消息
                            switch (targettype)
                            {
                                case 4://短信                        
                                    AFacade.SMSMessage.SendMessage(arm);
                                    break;
                                default:
                                    break;
                            }

                            //修改消息状态
                            arm.Status = "1";
                            arm.Try_times = arm.Try_times + 1;
                            arm.Send_time = DateTime.Parse(System.DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss"));
                            arm.Attach();
                            tran.Save<Models.app_resource_message>(arm);                    
                        
                    }

                    tran.Commit();
                }
                catch (Exception ex)
                {
                    tran.Rollback();
                    msg = ex.Message;
                }
            }
        }

        /// <summary>
        /// 时间戳转为C#格式时间
        /// </summary>
        /// <param name="timeStamp"></param>
        /// <returns></returns>
        private DateTime GetDatetimeFromTimeStamp(string timeStamp)
        {
            DateTime dateTimeStart = TimeZone.CurrentTimeZone.ToLocalTime(new DateTime(1970, 1, 1));
            long lTime = long.Parse(timeStamp + "0000000");
            TimeSpan toNow = new TimeSpan(lTime);

            return dateTimeStart.Add(toNow);
        }

        /// <summary>
        /// DateTime时间格式转换为Unix时间戳格式
        /// </summary>
        /// <param name="time"></param>
        /// <returns></returns>
        private int GetTimestampFromDatetime(System.DateTime time)
        {
            System.DateTime startTime = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1));
            return (int)(time - startTime).TotalSeconds;
        }


    }
}
