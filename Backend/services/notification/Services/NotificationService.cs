using Grpc.Core;
using notification;
using System.Net.Http;
using FirebaseAdmin;
using FirebaseAdmin.Messaging;
using Google.Apis.Auth.OAuth2;
using notification.Models;
using Newtonsoft.Json;

namespace notification.Services
{

    public class NotificationService : Notification.NotificationBase
    {
        private readonly ILogger<NotificationService> _logger;
        public NotificationService(ILogger<NotificationService> logger)
        {
            _logger = logger;
        }

        public override Task<NotifyReply> NotifyEventVitals(NotifyRequestVitals request, ServerCallContext context)
        {
            _logger.LogDebug(request.ToString());
            Console.WriteLine("Event: " + request.EventName + " occured with parameters: \n" + request.Params.ToString());

            SendNotification(request.EventName, request.Params.ToString());

            return Task.FromResult(new NotifyReply
            {
                Message = "Event " + request.EventName + " received."
            });
        }

        public override Task<NotifyReply> NotifyEventCalories(NotifyRequestCalories request, ServerCallContext context)
        {
            _logger.LogDebug(request.ToString());
            Console.WriteLine("Event: " + request.EventName + " occured with parameters: \n" + request.Params.ToString());

            SendNotification(request.EventName, request.Params.ToString());

            return Task.FromResult(new NotifyReply
            {
                Message = "Event " + request.EventName + " received."
            });
        }

        public override Task<NotifyReply> NotifyEventPulse(NotifyRequestPulse request, ServerCallContext context)
        {
            _logger.LogDebug(request.ToString());
            Console.WriteLine("Event: " + request.EventName + " occured with parameters: \n" + request.Params.ToString());

            SendNotification(request.EventName, request.Params.ToString());

            return Task.FromResult(new NotifyReply
            {
                Message = "Event " + request.EventName + " received."
            });
        }

        private Message CreateNotification(string title, string notificationBody, string token)
        {
            Message message = null;

            dynamic bodyObj = JsonConvert.DeserializeObject(notificationBody);
            if(bodyObj != null)
            {
                Console.WriteLine(bodyObj);
                if(title == "Average pulse" || title == "Max pulse")
                {
                    message = new Message()
                    {
                        Data = new Dictionary<string, string>()
                        {
                            {"event", title},
                            {"pulse", bodyObj.pulse}
                        },
                        Token = token,
                    };
                }
                else if(title == "High pulse"){

                    message = new Message()
                    {
                        Notification = new FirebaseAdmin.Messaging.Notification()
                        {
                            Body = $"Your heart rate is high. Last measured value is {bodyObj.pulse} BPM.",
                            Title = "High Heart Rate"
                        },
                        Token = token,
                    };
                }
                else if(title == "Low Pulse"){
                    message = new Message()
                    {
                        Notification = new FirebaseAdmin.Messaging.Notification()
                        {
                            Body = $"Your heart rate is low. Last measured value is {bodyObj.pulse} BPM.",
                            Title = "Low Heart Rate"
                        },
                        Token = token,
                    };

                }
                else if(title == "High pressure"){
                    message = new Message()
                    {
                        Notification = new FirebaseAdmin.Messaging.Notification()
                        {
                            Body = $"Your blood pressure is high. Last measured value is {bodyObj.sys}/{bodyObj.dias}.",
                            Title = "High Blood Pressure"
                        },
                        Token = token,
                    };

                }
                else if(title == "Low pressure"){
                    message = new Message()
                    {
                        Notification = new FirebaseAdmin.Messaging.Notification()
                        {
                            Body = $"Your blood pressure is low. Last measured value is {bodyObj.sys}/{bodyObj.dias}.",
                            Title = "Low Blood Pressure"
                        },
                        Token = token,
                    };

                }
                else if (title == "Burned calories"){
                    message = new Message()
                    {
                        Notification = new FirebaseAdmin.Messaging.Notification()
                        {
                            Body = $"You've burned {bodyObj.calories} calories. Keep going!",
                            Title = "Awesome Job!"
                        },
                        Token = token,
                    };
                }
            }
            Console.WriteLine(message.ToString());

            return message;
        }

        public async void SendNotification(string title, string body)
        {
            dynamic obj = JsonConvert.DeserializeObject(body);
            var userID = obj.userID;
            Console.WriteLine(userID);

            using (var httpClient = new HttpClient())
            {
                
                using (var response = await httpClient.GetAsync($"http://gateway:80/api/Gateway/GetFcm/{userID}"))
                {
                    Console.WriteLine(response.ToString());
                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var parameters = await response.Content.ReadFromJsonAsync<FcmToken>();
                        Console.Write(parameters);
                        if(parameters != null){
                            var resss = FirebaseMessaging.DefaultInstance.SendAsync(CreateNotification(title, body, parameters.Fcm)).Result;
                            Console.WriteLine(resss);
                        }
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        Console.WriteLine(errorResponse);
                    }
                }
            }

            
            //var result = await FirebaseMessaging.DefaultInstance.SendAsync(CreateNotification(title, body, token));
            //Console.WriteLine("Successfully sent message: " + result);


        }
    }
}