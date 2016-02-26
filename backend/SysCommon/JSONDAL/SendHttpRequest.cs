using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;
using System.IO;
using System.Data;
using Newtonsoft.Json.Linq;
namespace ws.data.jsonDal
{
    /// <summary>
    /// http通讯
    /// </summary>
    public class SendHttpRequest
    {
        /// <summary>
        /// 
        /// </summary>
        public static string strUrl = PMS.Sys.CSystem.baseSystem.str_restServer;

        /// <summary>
        /// 
        /// </summary>
        /// <param name="url"></param>
        /// <param name="param"></param>
        /// <param name="method"></param>
        /// <returns></returns>
        public static string SendRequest(string url, string param, string method = "POST")
        {
            string ls_rtn = "";
            //param=System.Web.HttpUtility.HtmlEncode(param);
            //param = System.Web.HttpUtility.UrlEncode(param);
            //byte[] data = Encoding.Unicode.GetBytes(param);
            byte[] data = Encoding.UTF8.GetBytes(param);
            HttpWebRequest request = (HttpWebRequest)HttpWebRequest.Create(url);            
            request.Method = method;// "POST";
            request.ContentType = "application/x-www-form-urlencoded";
            request.ContentLength = data.Length;
            request.KeepAlive = false;
            request.Timeout = 60 * 1000;
            string uid = PMS.Sys.CSystem.str_uid;
            string s_cook = PMS.Sys.CSystem.SHA1_Encrypt(uid + DateTime.Today.ToString("yyyyMMdd") + PMS.Sys.CSystem.myKey);
            request.CookieContainer = new CookieContainer();
            Cookie cook = new Cookie("token.wsdat", s_cook);
            cook.Domain = (new Uri(url)).Host;
            request.CookieContainer.Add(cook);            
            Stream sm = request.GetRequestStream();
            sm.Write(data, 0, data.Length);
            sm.Close();

            HttpWebResponse response = (HttpWebResponse)request.GetResponse();

            if (response.StatusCode.ToString() != "OK")
            {
                throw new Exception(response.StatusDescription.ToString());
            }

            StreamReader myreader = new StreamReader(
            response.GetResponseStream(), Encoding.UTF8);
            string responseText = myreader.ReadToEnd();
            ls_rtn = responseText;
            
            response.Close();
            response = null;

            return ls_rtn;
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="url"></param>
        /// <param name="json">{a:"aaa",b:"bbb"}</param>
        /// <returns></returns>
        public static string SendRequestByJson(string url, string json)
        {
            string ls_rtn = "";
            ls_rtn = SendRequest(url, JSTools.GetPostParamByJson(json));
            return ls_rtn;
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="json"></param>
        /// <returns></returns>
        public static string SendRequestByJson( string json)
        {
            string ls_rtn = "";
            if (strUrl == "") strUrl = "http://localhost:8514/wsdat.wsdat?callback=rtn";
            ls_rtn = SendRequest(strUrl, JSTools.GetPostParamByJson(json));
            return ls_rtn;
        }



        public static string Upload(string url, string filepath,ref string msg)
        {
            msg = "";
            try
            {
                string boundary = "CONST_FORM_BOUNDARY_TAG";
                string ls_rtn = "";
                // 创建request对象 
                HttpWebRequest webrequest = (HttpWebRequest)WebRequest.Create(url);
                webrequest.ContentType = "multipart/form-data; boundary=" + boundary;
                webrequest.Method = "POST";

                // 构造发送数据 
                StringBuilder sb = new StringBuilder();
                sb.AppendFormat("--{0}\r\n", boundary);
                sb.Append("Content-Disposition: form-data; name=\"icon\";filename=\"image.jpg\"");
                sb.Append("\r\n");
                sb.Append("Content-Type:image/jpeg");
                sb.Append("\r\n\r\n");

                string postHeader = sb.ToString();
                byte[] postHeaderBytes = Encoding.UTF8.GetBytes(postHeader);
                byte[] boundaryBytes = Encoding.ASCII.GetBytes("\r\n--" + boundary + "--\r\n");

                FileStream fileStream = new FileStream(filepath, FileMode.Open, FileAccess.Read);
                long length = postHeaderBytes.Length + fileStream.Length + boundaryBytes.Length;



                // 输入头部数据 
                //requestStream.Write(postHeaderBytes, 0, postHeaderBytes.Length);

                // 输入文件流数据 
                byte[] buffer = new Byte[fileStream.Length];
                fileStream.Read(buffer, 0, buffer.Length);

                byte[] buffer_all = postHeaderBytes.Concat(buffer).Concat(boundaryBytes).ToArray<byte>();

                webrequest.ContentLength = buffer_all.Length;
                Stream requestStream = webrequest.GetRequestStream();
                requestStream.Write(buffer_all, 0, buffer_all.Length);


                WebResponse responce = webrequest.GetResponse();
                Stream s = responce.GetResponseStream();
                StreamReader sr = new StreamReader(s);

                // 返回数据流(源码) 
                ls_rtn = sr.ReadToEnd();

                var obj = Newtonsoft.Json.JsonConvert.DeserializeObject<UploadResult>(ls_rtn);

                return obj.url;

            }
            catch (Exception ex)
            {
                msg = ex.Message;
                return null;
            }            
        }
    }

    /// <summary>
    /// {'url':'/upload1/2016-01-11/f6a333b1-720e-4c6e-80a1-3c610868e553.jpg','title':'','original':'','state':'SUCCESS'}
    /// </summary>
    public class UploadResult
    {
        public string url { get; set; }

        public string title { get; set; }

        public string original { get; set; }

        public string state { get; set; }

        
    }
}
