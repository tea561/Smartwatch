using System.ComponentModel.DataAnnotations;
using Newtonsoft.Json;

namespace notification.Models
{
    public class FcmToken
    {
        [JsonProperty("_id")]
        public int? _Id { get; set; }

        [JsonProperty("fcm")]
        public string? Fcm { get; set; }

    }
    
}