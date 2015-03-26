import twilio
import twilio.rest


SEND_TO = ''
SENT_FROM = ''
BODY_MESSAGE = ''


def GetAuthSIDAndAuthToken():
    accountSID = raw_input('Your Account SID: ')
    authToken = raw_input('Your Auth Token: ')
    return accountSID, authToken


def main(accountSID, authToken):
    try:
        client = twilio.rest.TwilioRestClient(accountSID, authToken)
        message = client.messages.create(body = BODY_MESSAGE,
                                         to = SEND_TO,
                                         from_ = SENT_FROM)

    except twilio.TwilioRestException as e:
        print e
    print message.sid



if __name__ == '__main__':
    accountSID, authToken = GetAuthSIDAndAuthToken()
    main(accountSID, authToken)
