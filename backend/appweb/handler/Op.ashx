<%@ WebHandler Language="C#" Class="Op" %>

using System;
using System.Web;
using System.Web.Script.Serialization;
using System.Net;
using System.IO;
public class Op : IHttpHandler, System.Web.SessionState.IRequiresSessionState 
{


    private HttpContext _context;
    public void ProcessRequest (HttpContext context) {
        _context = context;
        string ls_op = context.Request.Form["op"].ToString();
        string ls_rtn = "";
        if (ls_op.ToLower() == "send_checkcode")
        {
            string ls_phone = context.Request.Form["phone"].ToString();
            ls_rtn=SendCheckCode(ls_phone);          
        }


        if (ls_op.ToLower() == "regmaster")
        {
            string phone = context.Request.Form["phone"].ToString();
            string checkcode = context.Request.Form["checkcode"].ToString();
            string nick_name = context.Request.Form["nick_name"].ToString();
            string password = context.Request.Form["password"].ToString();
            string roomid = context.Request.Form["unit"].ToString();
            MasterController masterctr = new MasterController();
            String msg = "";
            masterctr.RegMaster(phone, checkcode, nick_name, password, roomid,ref msg);
            ls_rtn = msg;
        }
        
        
        
        
        context.Response.Write(ls_rtn);
    }

    public string SendCheckCode(string phone)
    {
        string ls_rtn = "";
        string s_phone = phone;
        string s_number = GetRandomString(5);//产生随机数
        string s_code = GetRandomString(5);//产生随机数
        _context.Session[phone] = s_code;//将手机号和随机数放置到session中，便于第二次提交注册数据时验证        
        try
        {
            ls_rtn = s_code;
            //发送短信息信息到指定的手机
            SendSMS(s_phone, "【科腾社区】注册,验证码" + s_code + ",短信编号" + s_number + "。");            
        }
        catch (Exception ex)
        {
            ls_rtn = "";              
        }
        return ls_rtn;
    }

    public static void SendSMS(string phonenumber, string content)
    {
        string strURL = "http://www.ztsms.cn/sendSms.do?username=keteng&password=6IDgPhVY&mobile=" + phonenumber + "&content=" + content + "&dstime=&productid=676767&xh=";
        WebRequest wRequest = WebRequest.Create(strURL);
        WebResponse wResponse = wRequest.GetResponse();
        Stream stream = wResponse.GetResponseStream();
        StreamReader reader = new StreamReader(stream, System.Text.Encoding.Default);
        string r = reader.ReadToEnd();
        wResponse.Close();
    }

    /// 生成8位随机数
    /// </summary>
    /// <param name="length"></param>
    /// <returns></returns>
    static public string GetRandomString(int len)
    {
        //string s = "123456789abcdefghijklmnpqrstuvwxyzABCDEFGHIJKLMNPQRSTUVWXYZ";
        string s = "1234567890";
        string reValue = string.Empty;
        Random rnd = new Random(getNewSeed());
        while (reValue.Length < len)
        {
            string s1 = s[rnd.Next(0, s.Length)].ToString();
            if (reValue.IndexOf(s1) == -1) reValue += s1;
        }
        return reValue;
    }

    private static int getNewSeed()
    {
        byte[] rndBytes = new byte[4];
        System.Security.Cryptography.RNGCryptoServiceProvider rng = new System.Security.Cryptography.RNGCryptoServiceProvider();
        rng.GetBytes(rndBytes);
        return BitConverter.ToInt32(rndBytes, 0);
    }     

 
    public bool IsReusable {
        get {
            return false;
        }
    }

}