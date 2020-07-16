import requests
import os
import json
import socket
import time
import logging

BACKEND_HOST = os.getenv('ACCEPTANCE_TEST_BACKEND_HOST', 'localhost')
BACKEND_PORT = os.getenv('ACCEPTANCE_TEST_BACKEND_PORT', '8080')
BACKEND_BASE_URL = 'http://{}:{}'.format(BACKEND_HOST, BACKEND_PORT)
TIMEOUT = 30

ALI_ALAVI_EMAIL = 'ali_alavi@gmail.com'
ALI_ALAVI_NEW_EMAIL = 'ali_alavi@yahoo.com'
ALI_ALAVI_PASSWORD = 'ali-alavi'
ALI_ALAVI_INTERESTS = 'Java, Python, Test, Docker, Jenkins, Prometheus, Grafana'

TAGHI_TAGHAVI_EMAIL = 'taghi_taghavi@gmail.com'
TAGHI_TAGHAVI_PASSWORD = 'taghi-taghavi'
TAGHI_TAGHAVI_INTERESTS = 'Java,Python,Test'

QUESTION_BODY = 'I want your help in my software test course project with Dr, Haghighi'
QUESTION_TOPICS = 'Java,Python,Test'
QUESTION_TYPE = 'PUBLIC'

ANSWER_TEXT = 'You can write a python code, that executes your API endpoints.'

COMMENT_TEXT = "Thank you"

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s', datefmt='%d-%b-%y %H:%M:%S')


def _check_connection():
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    return sock.connect_ex((BACKEND_HOST, int(BACKEND_PORT)))


def wait_for_services():
    i = 0
    while i < TIMEOUT:
        if _check_connection() == 0:
            logging.info('Application is now ready')
            return
        time.sleep(1)
        i = i + 1
        logging.warning('{}th Try failed'.format(i))
    raise Exception('Timeout to create connection to app. Is backend ready?')


def _convert_response_to_dict(response):
    content = response.content.decode('utf-8')
    return json.loads(content)


def register(email, password, interests, expected_result):
    request_body = {
        'email': email,
        'password': password,
        'interests': interests,
    }
    response = requests.post('{}/user/register'.format(BACKEND_BASE_URL), data=request_body)
    response_dict = _convert_response_to_dict(response)
    assert response_dict.get('success', False) is expected_result
    return response_dict['data']


def forget_password(email, expected_result):
    request_body = {
        'email': email
    }
    response = requests.post('{}/user/forget'.format(BACKEND_BASE_URL), data=request_body)
    response_dict = _convert_response_to_dict(response)
    assert response_dict.get('success', False) is expected_result
    return response_dict['data']


def login(email, password, expected_result):
    request_body = {
        'email': email,
        'password': password,
    }
    response = requests.post('{}/user/login'.format(BACKEND_BASE_URL), data=request_body)
    response_dict = _convert_response_to_dict(response)
    assert response_dict.get('success', False) is expected_result
    return response_dict['data']


def see_profile(email, page, expected_result):
    request_body = {
        'to': email,
        'page': page,
    }
    response = requests.post('{}/user/profile'.format(BACKEND_BASE_URL), data=request_body)
    response_dict = _convert_response_to_dict(response)
    assert response_dict.get('success', False) is expected_result
    return response_dict['data']


def edit_profile(email, password, token, expected_result):
    request_body = {
        'email': email,
        'password': password,
        'token': token,
    }
    response = requests.post('{}/user/edit'.format(BACKEND_BASE_URL), data=request_body)
    response_dict = _convert_response_to_dict(response)
    assert response_dict.get('success', False) is expected_result
    return response_dict['data']


def ask_question(body, topics, token, question_type, expected_result):
    request_body = {
        'body': body,
        'topics': topics,
        'token': token,
        'type': question_type,
    }
    response = requests.post('{}/questions/ask'.format(BACKEND_BASE_URL), data=request_body)
    response_dict = _convert_response_to_dict(response)
    assert response_dict.get('success', False) is expected_result
    return response_dict['data']


def get_questions_list(page, expected_result):
    response = requests.get('{}/questions/list/page/{}'.format(BACKEND_BASE_URL, page))
    response_dict = _convert_response_to_dict(response)
    assert response_dict.get('success', False) is expected_result
    questions = response_dict['data']
    assert len(questions) == 1
    return response_dict['data']


def get_question_page(question_id, page, expected_result):
    request_body = {
        'id': question_id,
        'page': page,
    }
    response = requests.post('{}/questions/question'.format(BACKEND_BASE_URL), data=request_body)
    response_dict = _convert_response_to_dict(response)
    assert response_dict.get('success', False) is expected_result
    return response_dict['data']


def set_answer(token, question_id, text, expected_result):
    request_body = {
        'token': token,
        'question': question_id,
        'text': text,
    }
    response = requests.post('{}/answers/set'.format(BACKEND_BASE_URL), data=request_body)
    response_dict = _convert_response_to_dict(response)
    assert response_dict.get('success', False) is expected_result
    return response_dict['data']


def set_comment(token, answer_id, body, expected_result):
    request_body = {
        'token': token,
        'answer': answer_id,
        'body': body,
    }
    response = requests.post('{}/comment/setForAns'.format(BACKEND_BASE_URL), data=request_body)
    response_dict = _convert_response_to_dict(response)
    assert response_dict.get('success', False) is expected_result
    return response_dict['data']


def like_answer(token, answer_id, expected_result):
    request_body = {
        'token': token,
        'answer': answer_id,
    }
    response = requests.post('{}/answers/UpVote'.format(BACKEND_BASE_URL), data=request_body)
    response_dict = _convert_response_to_dict(response)
    assert response_dict.get('success', False) is expected_result
    return response_dict['data']


if __name__ == '__main__':
    wait_for_services()
    register(ALI_ALAVI_EMAIL, ALI_ALAVI_PASSWORD, ALI_ALAVI_INTERESTS, True)
    forget_password(ALI_ALAVI_EMAIL, True)
    ali_alavi_user = login(ALI_ALAVI_EMAIL, ALI_ALAVI_PASSWORD, True)
    ali_alavi_token = ali_alavi_user['userId']
    see_profile(ALI_ALAVI_EMAIL, 1, True)
    ALI_ALAVI_EMAIL = ALI_ALAVI_NEW_EMAIL
    edit_profile(ALI_ALAVI_EMAIL, ALI_ALAVI_PASSWORD, ali_alavi_token, True)
    ask_question(QUESTION_BODY, QUESTION_TOPICS, ali_alavi_token, QUESTION_TYPE, True)
    taghi_taghavi_user = register(TAGHI_TAGHAVI_EMAIL, TAGHI_TAGHAVI_PASSWORD, TAGHI_TAGHAVI_INTERESTS, True)
    taghi_taghavi_token = taghi_taghavi_user['userId']
    question_list = get_questions_list(1, True)
    question = question_list[0]
    get_question_page(question['id'], 1, True)
    set_answer(taghi_taghavi_token, question['id'], ANSWER_TEXT, True)
    question_page = get_question_page(question['id'], 1, True)
    answer = question_page['answers'][0]
    set_comment(ali_alavi_token, answer['id'], COMMENT_TEXT, True)
    like_answer(ali_alavi_token, answer['id'], True)
