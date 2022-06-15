#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/*	Declare server IP	*/
#define SERVER_EP "coap://[fd00::1]:5683" 
#define REQ_INTERVAL 5

static int device_type = 0;	//0: sensor;	1: actuator;	2: both

/* Declare and auto-start this file's process */
PROCESS(device_process, "Sensor/Actuator Device");
AUTOSTART_PROCESSES(&device_process);



/*	Handler for the response from theserver	*/

void client_chunk_handler(coap_message_t *response) {
 
	if(response == NULL) { 
		printf("Request timed out\n"); 
		return;
	}
	const uint8_t *chunk;
	coap_get_payload(response, &chunk);
	printf("Received Response: %s\n", (char *)chunk);

	//If the response is "Accept" I registered the device
	if(strcmp( (char *)chunk, "Accept") == 0)
		registration_status = true;
	else
		registration_status = false;

}


PROCESS_THREAD(device_process, ev, data){

	static coap_endpoint_t server_ep;
  	static coap_message_t request[1];  /* the packet can be treated as pointer */

	PROCESS_BEGIN();

	/*	Get device type from command line and activate the related resources	*/
	while(1){
		printf("\nType the kind of device you want to deploy: \"sensor\", \"actuator\", \"both\"\n");
		PROCESS_WAIT_EVENT_UNTIL(ev == serial_line_event_message);
		//Sensor => I activate sensor resources
		if(strcmp(data, "sensor") == 0){
			//No led needed
			device_type = 0;

			/*	Resource activation	*/
			//coap_activate_resource(&res_humidity, "humidity");
			break;
		}
		//Actuator => I activate actuator resources
		else if(strcmp(data, "actuator") == 0){
			device_type = 1;

			/*	Resource activation	*/
			//coap_activate_resource(&res_sprinkler, "sprinkler");
			break;
		}
		//Both => I activate all resources
		else if(strcmp(data, "both") == 0){
			device_type = 2;

			/*	Resource activation	*/

			//sensors
			//coap_activate_resource(&res_humidity, "humidity");


			//actuators
			//coap_activate_resource(&res_sprinkler, "sprinkler");

			break;
		}
	}


	while(1){

		//Possible events: timer, unregister event, serial line message
		PROCESS_WAIT_EVENT_UNTIL(ev == PROCESS_EVENT_TIMER || ev == UNREGISTER || ev == serial_line_event_message);


	}
  	PROCESS_END();
}
