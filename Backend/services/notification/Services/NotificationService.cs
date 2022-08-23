using Grpc.Core;
using notification;
using FirebaseAdmin;
using FirebaseAdmin.Messaging;
using Google.Apis.Auth.OAuth2;


namespace notification.Services
{

    public class NotificationService : Notification.NotificationBase
    {
        private readonly ILogger<NotificationService> _logger;
        public NotificationService(ILogger<NotificationService> logger)
        {
            _logger = logger;
        }

        public override Task<NotifyReply> NotifyEvent(NotifyRequest request, ServerCallContext context)
        {
            _logger.LogDebug(request.ToString());
            Console.WriteLine("Event: " + request.EventName + " occured with parameters: \n" + request.Params.ToString());

            SendNotification("title", "body body body", "cI1t6HIj6FbCgQ8gYDDszn:APA91bGfS4JnHeTXWCAa3vjq_FPLoaF9jGiaoraknLDU2A-uc9n_QM-9TJqr8A8px2AT_UjrXrL_qR2C3XhHOKvplDNgwCGtivu8gqzZ2qd5y0fTvWBYMLfcc3QWasZruFvltDPWr9w5");

            Console.WriteLine("CALLED");

            return Task.FromResult(new NotifyReply
            {
                Message = "Event " + request.EventName + " received."
            });
        }

        private Message CreateNotification(string title, string notificationBody, string token)
        {
            var message = new Message()
            {
                Data = new Dictionary<string, string>()
    {
        { "score", "850" },
        { "time", "2:45" },
    },
        Notification = new FirebaseAdmin.Messaging.Notification()
        {
            Body = notificationBody,
            Title = title
        },
                Token = token,
            };
            return message;
        }

        public void SendNotification(string title, string body, string token)
        {
            Console.WriteLine("FROM SEND");
            var resss = FirebaseMessaging.DefaultInstance.SendAsync(CreateNotification(title, body, token)).Result;
            Console.WriteLine(resss);
            //var result = await FirebaseMessaging.DefaultInstance.SendAsync(CreateNotification(title, body, token));
            //Console.WriteLine("Successfully sent message: " + result);


        }


    }
}