CONTIKI_PROJECT = device
all: $(CONTIKI_PROJECT)

MODULES_REL += ./resources
CFLAGS += -DPROJECT_CONF_H=\"project-conf.h\"

CONTIKI=../../..


include $(CONTIKI)/Makefile.dir-variables
MODULES += $(CONTIKI_NG_APP_LAYER_DIR)/coap

include $(CONTIKI)/Makefile.include
                           
