import requests, argparse


parser = argparse.ArgumentParser()
parser.add_argument('phrase', help='phrase to be searched')
args = parser.parse_args()

obj = requests.get('http://api.conceptnet.io/c/en/' + args.phrase).json()

edges = obj['edges']


for edge in edges:
    if 'CapableOf' in edge['@id']:
        print(edge['start']['label'])
        exit(0)
