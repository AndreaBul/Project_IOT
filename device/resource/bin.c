#include "contiki.h"
#include "coap-engine.h"
#include <string.h>
#include "os/dev/leds.h"

/* Log configuration */
#include "sys/log.h"
#define LOG_MODULE "Light"
#define LOG_LEVEL LOG_LEVEL_APP

bool light_status = false;
extern bool bin_status;
static unsigned int g_accept = APPLICATION_JSON;
static unsigned int p_accept = APPLICATION_JSON;

static void res_get_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset);
static void res_post_put_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset);
static void res_event_handler(void);

/*----------------------------------------------------------------------------------------------*/

EVENT_RESOURCE(bin,
         "title=\"Bin \";methods=\"GET/POST\;rt=\"actuator\"\n",
	       res_get_handler,
         res_post_put_handler,
         res_post_put_handler,
         NULL, 
	res_event_handler);
        
/*----------------------------------------------------------------------------------------------*/

static void res_event_handler(void) {
 
    coap_notify_observers(&res_light);
}


/* Light Actuator: it returns its status */


static void res_get_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset){
	
	char response_message[COAP_MAX_CHUNK_SIZE];

	coap_get_header_accept(request, &g_accept);
	//Can handle only JSON format
	if(g_accept == APPLICATION_JSON){
		
		coap_set_header_content_format(response, APPLICATION_JSON);
		
		//take the status
		int len = snprintf(response_message, COAP_MAX_CHUNK_SIZE, "{\"light\":%s}", light_status ? "ON" : "OFF");
		if(len > 0){
			//prepare the message
			memcpy(buffer, (uint8_t*)response_message, len);
			coap_set_header_etag(response, (uint8_t *)&len, 1);
      coap_set_payload(response, buffer, len); 
		}else
			LOG_INFO("Error: Couldn't form the response\n");
	}else{
		coap_set_status_code(response, NOT_ACCEPTABLE_4_06);
	  const char *msg = "The only supported type is json";
	  coap_set_payload(response, msg, strlen(msg));
	}
}


static void res_post_put_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset){


	if(request == NULL){

			LOG_INFO("[LIGHT]: Empty request\n");
			return;
	}

	coap_get_header_accept(request, &post_accept);

	if(post_accept == APPLICATION_JSON){

		size_t pay_len = 0;
		char *new_status = NULL;
		char *var = NULL;
		bool good_req = false;

		const uint8_t **message;
		message = malloc(request->payload_len);

		if(message == NULL){
			LOG_INFO("[LIGHT]: Empty payload\n");
			return;
		}

		pay_len = coap_get_payload(request, message);
		LOG_INFO("Message received: %s\n", (char *)*message);

		if(pay_len > 0){
		
			//get new value. 

		}


		//Check if variable and new_status are not null
		if(var != NULL && new_status != NULL){




		}
    
		//Send the response
		if(good_req)
			coap_set_status_code(response, CHANGED_2_04);

		if(!good_req)
			coap_set_status_code(response, BAD_REQUEST_4_00);		
		
	}else{
		coap_set_status_code(response, NOT_ACCEPTABLE_4_06);
	  const char *msg = "The only supported type is json";
	  coap_set_payload(response, msg, strlen(msg));
	}


}
