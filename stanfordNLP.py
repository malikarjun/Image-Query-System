import requests, argparse,re,time,json
from selenium import webdriver
from selenium.webdriver.support.ui import Select



parser = argparse.ArgumentParser()
parser.add_argument('caption', help='caption to be searched')
parser.add_argument('phrase', help='phrase to be searched')
args = parser.parse_args()




driver = webdriver.PhantomJS()

driver.get('http://nlp.stanford.edu:8080/corenlp/process')


# navigate to the page
select = Select(driver.find_element_by_name('outputFormat'))
select.select_by_visible_text('JSON')

input_textbox = driver.find_element_by_name('input')
caption = args.caption.replace('_',' ')
phrase = args.phrase.replace('_', ' ')
# print(caption)
input_textbox.send_keys(caption)

submit_button = driver.find_element_by_name('Process')
submit_button.click()

json_output = driver.find_element_by_xpath('//div//pre')
val = json_output.text


val = json.loads(val)
root = val['sentences'][0]['basic-dependencies'][0]['dependentGloss']

right = re.split(root, caption)[1]
left = re.split(root, caption)[0]

if phrase not in right:
    print('left')
    print(root + right)
else:
    print('right')
    print(left + root)



