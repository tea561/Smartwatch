using Microsoft.AspNetCore.Mvc;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using Google.Apis.YouTube.v3;
using Google.Apis.Services;
using gateway.Models;
using Microsoft.Extensions.Configuration;
using MQTTnet;
using MQTTnet.Client;


namespace gateway.Controllers
{
    [ApiController]
    [Route("/api/[controller]")]

    public class GatewayController : ControllerBase
    {
        private readonly IConfiguration _configuration;
        public GatewayController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        #region GetHealthParameters

        /// <summary>
        /// Gets parameters for specific user.
        /// </summary>
        /// <param name="userID">User ID</param>
        /// <returns>Vitals.</returns>
        /// <response code="404">User not found.</response>
        [HttpGet("GetParameters/{userID}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> GetParameters(int userID)
        {
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://data:3333/getVitals?userID={userID}"))
                {

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var parameters = await response.Content.ReadFromJsonAsync<Parameters>();
                        return Ok(parameters);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
            }
        }
        

        /// <summary>
        /// Gets user's health parameters.
        /// </summary>
        /// <param name="userID">User ID</param>
        /// <returns>Health parameters.</returns>
        /// <response code="404">User not found.</response>
        [HttpGet("GetAllParameters/{userID}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> GetAllParameters(int userID)
        {
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://data:3333/getAllParameters?userID={userID}"))
                {

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var parameters = await response.Content.ReadFromJsonAsync<HealthParameters>();
                        return Ok(parameters);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
            }
        }

        /// <summary>
        /// Gets values of one parameter for today.
        /// </summary>
        /// <param name="userID">User ID</param>
        /// <param name="param">Name of parameter</param>
        /// <returns>Value of parameter.</returns>
        /// <response code="404">User not found.</response>
        [HttpGet("GetParameterForDay/{param}/{userID}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> GetParameterForDay(string param, int userID)
        {
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://data:3333/getParameterForDay?userID={userID}&param={param}"))
                {

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var parameters = await response.Content.ReadFromJsonAsync<List<OneParameter>>();
                        return Ok(parameters);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
            }
        }

        /// <summary>
        /// Gets values of one parameter for week.
        /// </summary>
        /// <param name="userID">User ID</param>
        /// <param name="param">Name of parameter</param>
        /// <returns>Values of parameter.</returns>
        /// <response code="404">User not found.</response>
        [HttpGet("GetParameterForWeek/{param}/{userID}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> GetParameterForWeek(string param, int userID)
        {
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://data:3333/getParameterForWeek?userID={userID}&param={param}"))
                {

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var parameters = await response.Content.ReadFromJsonAsync<List<OneParameter>>();
                        return Ok(parameters);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
            }
        }

        /// <summary>
        /// Gets sum of all values for today.
        /// </summary>
        /// <param name="userID">User ID</param>
        /// <param name="param">Name of parameter</param>
        /// <returns>Values of parameter.</returns>
        /// <response code="404">User not found.</response>
        [HttpGet("GetDailySum/{param}/{userID}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> GetDailySum(string param, int userID)
        {
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://data:3333/getDailySum?userID={userID}&param={param}"))
                {

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var parameters = await response.Content.ReadFromJsonAsync<List<OneParameter>>();
                        return Ok(parameters);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
            }
        }
        /// <summary>
        /// Gets values for one week.
        /// </summary>
        /// <param name="userID">User ID</param>
        /// <param name="param">Name of parameter</param>
        /// <returns>Values of parameter.</returns>
        /// <response code="404">User not found.</response>
        [HttpGet("GetDailySumForWeek/{param}/{userID}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> GetDailySumForWeek(string param, int userID)
        {
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://data:3333/getDailySumForWeek?userID={userID}&param={param}"))
                {

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var parameters = await response.Content.ReadFromJsonAsync<List<OneParameter>>();
                        return Ok(parameters);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
            }
        }

        #endregion

        /// <summary>
        /// Gets user's friends.
        /// </summary>
        /// <param name="userID">User ID</param>
        /// <returns>List of friends.</returns>
        /// <response code="404">User not found.</response>
        [HttpGet("GetFriends/{userID}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> GetFriends(int userID)
        {
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://users:6080/user/getFriends/{userID}"))
                {

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var parameters = await response.Content.ReadFromJsonAsync<List<User>>();
                        return Ok(parameters);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
            }
        }


        /// <summary>
        /// Gets specific user.
        /// </summary>
        /// <param name="userID">User ID</param>
        /// <returns>User.</returns>
        /// <response code="404">User not found.</response>
        [HttpGet("GetUser/{userID}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> GetUser(int userID)
        {
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://users:6080/user/{userID}"))
                {

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var parameters = await response.Content.ReadFromJsonAsync<User>();
                        return Ok(parameters);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
            }
        }

        /// <summary>
        /// Gets FCM token for specific user.
        /// </summary>
        /// <param name="userID">User ID</param>
        /// <returns>User.</returns>
        /// <response code="404">User not found.</response>
        [HttpGet("GetFcm/{userID}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> GetFcm(int userID)
        {
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://users:6080/user/getFcm/{userID}"))
                {

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var parameters = await response.Content.ReadFromJsonAsync<FcmToken>();
                        return Ok(parameters);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
            }
        }

        /// <summary>
        /// Add FCM token for specific user.
        /// </summary>
        /// <param name="fcmToken">User ID and FCM</param>
        /// <returns>True if data is successfully added. Otherwise, false.</returns>
        /// <remarks>
        ///     Sample request:
        ///         
        ///         POST /Gateway
        ///         {
        /// {
        ///     "_id": 2,
        ///     "fcm": "32fsd13214rfe"
        /// }
        ///         }
        ///
        ///</remarks>
        /// <response code="400">Post parameters not defined.</response>
        /// <response code="200">Data successfully added.</response>

        [HttpPost("PostFcm")]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> PostFcm([FromBody] FcmToken fcmToken)
        {
            using (var httpClient = new HttpClient())
            {
                var serializedObject = JsonConvert.SerializeObject(fcmToken);
                var content = new StringContent(serializedObject, Encoding.UTF8, "application/json");
                Console.WriteLine(serializedObject);
            
                using (var response = await httpClient.PostAsync("http://users:6080/user/addFcm", content))
                {
                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var apiResponse = await response.Content.ReadFromJsonAsync<int>();
                        //await this.PublishVitals(serializedObject);

                        return Ok();
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }

                }

                return BadRequest();
            }
        }

            /// <summary>
        /// Update FCM token for specific user.
        /// </summary>
        /// <param name="fcm">User ID and FCM</param>
        /// <returns>True if data is successfully added. Otherwise, false.</returns>
        /// <remarks>
        ///     Sample request:
        ///         
        ///         POST /Gateway
        ///         {
        /// {
        ///     "_id": 2,
        ///     "fcm": "32fsd13214rfe"
        /// }
        ///         }
        ///
        ///</remarks>
        /// <response code="400">Put parameters not defined.</response>
        /// <response code="200">Data successfully added.</response>

        [HttpPost("PutFcm")]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> PutFcm([FromBody] FcmToken fcm)
        {
            using (var httpClient = new HttpClient())
            {
                var serializedObject = JsonConvert.SerializeObject(fcm);
                var content = new StringContent(serializedObject, Encoding.UTF8, "application/json");
            
                using (var response = await httpClient.PutAsync("http://users:6080/user/updateFcm", content))
                {
                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var apiResponse = await response.Content.ReadFromJsonAsync<int>();
                        //await this.PublishVitals(serializedObject);
                        return Ok();
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }

                }

                return BadRequest();
            }
        }


        /// <summary>
        /// Gets vitals and recommended YouTube resource for specific user.
        /// </summary>
        /// <param name="userID">User ID</param>
        /// <returns>Vitals and recommended YouTube resource.</returns>
        /// <response code="404">User not found.</response>
        [HttpGet("GetStatus/{userID}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> GetStatus(int userID)
        {

            ResultData resultData = new ResultData();
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://data:3333/getVitals?userID={userID}"))
                {

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var parameters = await response.Content.ReadFromJsonAsync<Parameters>();
                        resultData.HealthParameters = parameters;
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
            }

            string searchTerm = "neutral";

            if (resultData.HealthParameters?.Sys > 130 || resultData.HealthParameters?.Dias > 90)
            {
                if (resultData.HealthParameters?.Pulse > 80)
                    searchTerm = "calm";
                else
                    searchTerm = "classical";
            }
            else if (resultData.HealthParameters?.Pulse > 80)
            {
                searchTerm = "relaxing";
            }
            else if (resultData.HealthParameters?.Pulse < 60 || resultData.HealthParameters?.Sys < 120 || resultData.HealthParameters?.Dias < 70)
            {
                searchTerm = "upbeat";
            }

            var youtubeService = new YouTubeService(new BaseClientService.Initializer()
            {
                ApiKey = _configuration["ApiKey"],
                ApplicationName = this.GetType().ToString()
            });



            var searchListRequest = youtubeService.Search.List("snippet");
            searchListRequest.Q = QueryGenerator.QueryGenerator.getQueryParameter(searchTerm);
            searchListRequest.MaxResults = 1;
            searchListRequest.Type = "video,playlist";

            var searchListResponse = await searchListRequest.ExecuteAsync();

            resultData.ResourceTitle = searchListResponse.Items[0].Snippet.Title;
            string resourceId;

            resourceId = (searchListResponse.Items[0].Id.Kind == "youtube#playlist") ? searchListResponse.Items[0].Id.PlaylistId : searchListResponse.Items[0].Id.VideoId;
            resultData.ResourceUrl = $"https://www.youtube.com/watch?v={resourceId}";

            return Ok(resultData);

        }

        /// <summary>
        /// Post vital parameters for specific user.
        /// </summary>
        /// <param name="parameters">Vital parameters</param>
        /// <returns>True if vitals are successfully added. Otherwise, false.</returns>
        /// <remarks>
        ///     Sample request:
        ///         
        ///         POST /Gateway
        ///         {
        ///             "sys": 120,
        ///             "dias": 80,
        ///             "pulse": 90,
        ///             "timestamp": 1652119779,
        ///             "userID": 3
        ///         }
        ///
        ///</remarks>
        /// <response code="400">Post parameters not defined.</response>
        /// <response code="200">Vitals successfully added.</response>

        [HttpPost("PostVitals")]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> Post([FromBody] Parameters parameters)
        {
            using (var httpClient = new HttpClient())
            {
                var serializedObject = JsonConvert.SerializeObject(parameters);
                var content = new StringContent(serializedObject, Encoding.UTF8, "application/json");
                using (var response = await httpClient.PostAsync("http://data:3333/postVitals", content))
                {
                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var apiResponse = await response.Content.ReadFromJsonAsync<bool>();
                        Console.WriteLine("MQTT VITALS" + serializedObject);
                        await this.PublishVitals("projekat/vitals", serializedObject);
                        return Ok(apiResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }

                }

                return BadRequest();
            }
        }

        /// <summary>
        /// Post sleep hours for specific user.
        /// </summary>
        /// <param name="parameters">Sleep hours and date</param>
        /// <returns>True if data is successfully added. Otherwise, false.</returns>
        /// <remarks>
        ///     Sample request:
        ///         
        ///         POST /Gateway
        ///         {
        ///             "sleepHours": "7:30:20",
        ///             "timestamp": 1652119779,
        ///             "userID": 3
        ///         }
        ///
        ///</remarks>
        /// <response code="400">Post parameters not defined.</response>
        /// <response code="200">Data successfully added.</response>

        [HttpPost("PostSleepHours")]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> PostSleepHours([FromBody] SleepData sleepHours)
        {
            using (var httpClient = new HttpClient())
            {
                Console.WriteLine("OK");
                var serializedObject = JsonConvert.SerializeObject(sleepHours);
                var content = new StringContent(serializedObject, Encoding.UTF8, "application/json");
                using (var response = await httpClient.PostAsync("http://data:3333/postSleepHours", content))
                {
                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var apiResponse = await response.Content.ReadFromJsonAsync<bool>();
                        Console.WriteLine("MQTT SLEEP HOURS", serializedObject);
                        await this.PublishVitals("projekat/sleepHours", serializedObject);
                        return Ok(apiResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }

                }

                return BadRequest();
            }
        }

        /// <summary>
        /// Post sleep hours for specific user.
        /// </summary>
        /// <param name="steps">Steps and date</param>
        /// <returns>True if data is successfully added. Otherwise, false.</returns>
        /// <remarks>
        ///     Sample request:
        ///         
        ///         POST /Gateway
        ///         {
        ///             "steps": 1233,
        ///             "time": "22 Jan 2017 12:34",
        ///             "userID": 3
        ///         }
        ///
        ///</remarks>
        /// <response code="400">Post parameters not defined.</response>
        /// <response code="200">Data successfully added.</response>

        [HttpPost("PostSteps")]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> PostSteps([FromBody] OneParameter steps)
        {
            using (var httpClient = new HttpClient())
            {
                Console.WriteLine("OK");
                var serializedObject = JsonConvert.SerializeObject(steps);
                var content = new StringContent(serializedObject, Encoding.UTF8, "application/json");
                using (var response = await httpClient.PostAsync("http://data:3333/postSteps", content))
                {
                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var apiResponse = await response.Content.ReadFromJsonAsync<bool>();
                        Console.WriteLine("MQTT STEPS", serializedObject);
                        await this.PublishVitals("projekat/steps", serializedObject);
                        var obj = new
                        {
                            result = apiResponse
                        };
                        return Ok(obj);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }

                }

                return BadRequest();
            }
        }

        /// <summary>
        /// Post user.
        /// </summary>
        /// <param name="user">User data</param>
        /// <returns>True if data is successfully added. Otherwise, false.</returns>
        /// <remarks>
        ///     Sample request:
        ///         
        ///         POST /Gateway
        ///         {
        /// {
        ///     "_id": 2,
        ///     "username": "user23",
        ///     "age": 27,
        ///     "gender": "m",
        ///     "height": 193,
        ///     "weight": 90
        /// }
        ///         }
        ///
        ///</remarks>
        /// <response code="400">Post parameters not defined.</response>
        /// <response code="200">Data successfully added.</response>

        [HttpPost("PostUser")]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> PostUser([FromBody] User user)
        {
            using (var httpClient = new HttpClient())
            {
                var serializedObject = JsonConvert.SerializeObject(user);
                var content = new StringContent(serializedObject, Encoding.UTF8, "application/json");
            
                using (var response = await httpClient.PostAsync("http://users:6080/user", content))
                {
                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var apiResponse = await response.Content.ReadFromJsonAsync<int>();
                        //await this.PublishVitals(serializedObject);
                        var obj = new
                        {
                            result = apiResponse
                        };
                        return Ok(obj);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }

                }

                return BadRequest();
            }
        }


        /// <summary>
        /// Calculate burned calories and save in db.
        /// </summary>
        /// <param name="activityParameters">Activity parameters</param>
        /// <returns>True if data is successfully added. Otherwise, false.</returns>
        /// <remarks>
        ///     Sample request:
        ///         Parameters
        ///         POST /Gateway
        ///         {
        /// {
        ///     "_id": 2,
        ///     "Duration": 23.3,
        ///     "Heart_Rate": 110.0,
        ///     "Body_Temp": 40.0 ,
        /// }
        ///         }
        ///
        ///</remarks>
        /// <response code="400">Post parameters not defined.</response>
        /// <response code="200">Data successfully added.</response>
        [HttpPost("CalculateCalories")]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> CalculateCalories([FromBody] ActivityParameters activityParameters)
        {
            User? user = new User();
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://users:6080/user/{activityParameters.UserID}"))
                {

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        user = await response.Content.ReadFromJsonAsync<User>();    

                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }

                if (user == null)
                    return BadRequest();

                var obj = new
                {
                    Gender = user.Gender.Equals("m") ? 0 : 1,
                    Age = user.Age,
                    Height = user.Height,
                    Weight = user.Weight,
                    Duration = activityParameters.Duration,
                    Heart_Rate = activityParameters.Heart_Rate,
                    Body_Temp = activityParameters.Body_Temp
                };
                Console.WriteLine(activityParameters);
                Console.WriteLine(obj);

                var serializedObject = JsonConvert.SerializeObject(obj);
                var content = new StringContent(serializedObject, Encoding.UTF8, "application/json");
            

                float calories = -1.0f;
                using (var response = await httpClient.PostAsync("http://calories:7080/predict", content))
                {
                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        calories = await response.Content.ReadFromJsonAsync<float>();
                        Console.WriteLine(calories);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }

                }

                if(calories != -1.0f)
                {
                    var caloriesObj = new {
                        calories = calories,
                        userID = activityParameters.UserID
                    };
                    var serializedObjCalories = JsonConvert.SerializeObject(caloriesObj);
                    Console.WriteLine("MQTT CALORIES" + serializedObjCalories);
                    await PublishVitals("projekat/calories", serializedObjCalories);
                    var putData = new {
                        _id = activityParameters.UserID,
                        points = calories / 10
                    };
                    var serializedPoints = JsonConvert.SerializeObject(putData);
                    var putContent = new StringContent(serializedPoints, Encoding.UTF8, "application/json");
                    using(var response = await httpClient.PutAsync("http://users:6080/user/addPoints", putContent))
                    {
                        if (response.StatusCode == System.Net.HttpStatusCode.OK)
                        {
                            var resp = await response.Content.ReadFromJsonAsync<double>();
                        }
                        else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                        {
                            var errorResponse = await response.Content.ReadAsStringAsync();
                            return NotFound(errorResponse);
                        }
                        else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                        {
                            return StatusCode(500);
                        }
                    }

                    var postData = new {
                        calories = calories,
                        userID = activityParameters.UserID
                    };

                    var serializedData = JsonConvert.SerializeObject(postData);
                    var postContent = new StringContent(serializedData, Encoding.UTF8, "application/json");

                    using (var response = await httpClient.PostAsync("http://data:3333/postCalories", postContent))
                    {
                        Console.WriteLine("DATA POST");
                        if (response.StatusCode == System.Net.HttpStatusCode.OK)
                        {
                            var resp = await response.Content.ReadFromJsonAsync<bool>();
                            return Ok(resp);
                        }
                        else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                        {
                            var errorResponse = await response.Content.ReadAsStringAsync();
                            return NotFound(errorResponse);
                        }
                        else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                        {
                            return StatusCode(500);
                        }

                    }
                    
                }

                return BadRequest();


            }
        }


        /// <summary>
        /// Delete vital parameters for specific user.
        /// </summary>
        /// <param name="userID">User ID</param>
        /// <returns>True if vitals are successfully added. Otherwise, false.</returns>
        /// <response code="200">Vitals successfully deleted.</response>
        [HttpDelete("DeleteDataForUser/{userID}")]
        public async Task<ActionResult> DeleteDataForUser(int userID)
        {
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.DeleteAsync($"http://data:3333/deleteVitals?userID={userID}"))
                {
                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        return Ok();
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
            }
        }

        /// <summary>
        /// Update vital parameters for specific user.
        /// </summary>
        /// <param name="parameters">Vital parameters</param>
        /// <returns>True if vitals are successfully updated. Otherwise, false.</returns>
        /// <response code="400">Put parameters not defined.</response>
        /// <response code="200">Vitals successfully added.</response>
        /// <remarks>
        ///     Sample request:
        ///         
        ///         PUT /Gateway
        ///         {
        ///             "sys": 120,
        ///             "dias": 80,
        ///             "pulse": 90,
        ///             "timestamp": 1652119779,
        ///             "userID": 3
        ///         }
        ///
        ///</remarks>
        [HttpPut("UpdateVitals")]
        public async Task<ActionResult> Put([FromBody] Parameters parameters)
        {
            using (var httpClient = new HttpClient())
            {
                var serializedObject = JsonConvert.SerializeObject(parameters);
                StringContent content = new StringContent(serializedObject, Encoding.UTF8, "application/json");
                using (var response = await httpClient.PutAsync("http://data:3333/updateVitals", content))
                {
                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var apiResponse = await response.Content.ReadFromJsonAsync<bool>();
                        return Ok(apiResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                }

                return BadRequest();
            }
        }

        /// <summary>
        /// Add friend.
        /// </summary>
        /// <param name="userID">First user</param>
        /// <param name="friendID">Second user</param>

        /// <returns>True if friend relationship is saved. Otherwise, false.</returns>
        /// <response code="400">Put parameters not defined.</response>
        /// <response code="200">Friend relationship added.</response>
        [HttpPut("AddFriend/{userID}/{friendID}")]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<ActionResult> AddFriend(int userID, int friendID)
        {
            using (var httpClient = new HttpClient())
            {
                //var serializedObject = JsonConvert.SerializeObject(parameters);
                //StringContent content = new StringContent(serializedObject, Encoding.UTF8, "application/json");
                using (var response = await httpClient.PutAsync($"http://users:6080/user/addFriend/{userID}/{friendID}", null))
                {
                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var apiResponse = await response.Content.ReadFromJsonAsync<User>();
                        return Ok(apiResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                }

                return BadRequest();
            }
        }

        private async Task PublishVitals(string topic, string payload)
        {
            var mqttFactory = new MqttFactory();

            using (var mqttClient = mqttFactory.CreateMqttClient())
            {
                var mqttClientOptions = new MqttClientOptionsBuilder()
                    .WithTcpServer("mqtt", 1883)
                    .WithClientId("gateway")
                    .Build();

                mqttClient.ConnectedAsync += conn =>
                {
                    Console.WriteLine("The MQTT client is connected.");
                    return Task.CompletedTask;
                };
                mqttClient.DisconnectedAsync += disc =>
                {
                    Console.WriteLine("The MQTT client is disconnected.");
                    return Task.CompletedTask;
                };

                var response = await mqttClient.ConnectAsync(mqttClientOptions, CancellationToken.None);
                Console.WriteLine(response.ResultCode);

                var applicationMessage = new MqttApplicationMessageBuilder()
                    .WithTopic(topic)
                    .WithPayload(payload)
                    .Build();
                await mqttClient.PublishAsync(applicationMessage, CancellationToken.None);
                Console.WriteLine("MQTT application message is published.");

                var disconnectOptions = new MqttClientDisconnectOptionsBuilder()
                    .WithReason(MqttClientDisconnectReason.NormalDisconnection)
                    .Build();
                await mqttClient.DisconnectAsync(disconnectOptions);

            }
        }
    }
}