using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Web;

namespace MSGSrv.AFacade
{
    public class SMSMessage
    {
        public static void SendMessage(Models.app_resource_message arm)
        {            
            try
            {
                if (arm.Status == "0")
                {
                    string s_password = "8Uy5%*lM4";
                    s_password = System.Web.Security.FormsAuthentication.HashPasswordForStoringInConfigFile(s_password, "MD5").ToLower();                    
                    string strURL = "http://www.ztsms.cn/sendSms.do?username=keteng&password=" + s_password + "&mobile=" + arm.Target + "&content=" + arm.Info + "&dstime=&productid=676767&xh=";
                    WebRequest wRequest = WebRequest.Create(strURL);
                    WebResponse wResponse = wRequest.GetResponse();
                    Stream stream = wResponse.GetResponseStream();
                    StreamReader reader = new StreamReader(stream, System.Text.Encoding.Default);
                    string r = reader.ReadToEnd();
                    wResponse.Close();
                }                
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }
    }
}