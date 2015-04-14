import uuid
import hashlib

def hash_password(password):
    #uuid is used to generate a random number
    salt = uuid.uuid4().hex
    return hashlib.sha256(salt.encode() + password.encode()).hexdigest() + ':' + salt

def check_password(hashed_password, user_password):
    password, salt = hashed_password.split(':')
    return password == hashlib.sha256(salt.encode() + user_password.encode()).hexdigest()



def main():
    new_pass = raw_input('Please enter a password: ')
    hashed_password = hash_password(new_pass)
    print (' The string to store in the db is: ' + hashed_password)

    old_pass = raw_input('Now please enter the password again to check: ')
    if check_password(hashed_password, old_pass):
        print ('You entered the right password')
    else:
        print('I\'m sorry, but the password does not match')


if __name__ == '__main__':
    main()
