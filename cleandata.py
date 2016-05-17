# -*- coding: utf-8 -*-
"""
Created on Tue May 17 13:24:19 2016

@author: catherineordun
"""

import nltk 
from nltk.tokenize import LineTokenizer
#read in file
raw = open('/Users/catherineordun/Documents/corpus/facilities.txt').read()
#tokenizefile and remove white spaces
LineTokenizer(blanklines = 'discard').tokenize(raw)

#removes tokens with no periods
from nltk.tokenize import RegexpTokenizer
tokens = RegexpTokenizer("[\w']+")
tokens.tokenize(raw)
rawtokens = tokens.tokenize(raw)

#print out the tokens to a text file
from __future__ import print_function
log = open("/Users/catherineordun/Desktop/rawtokens.txt", "w")
print(rawtokens, file = log)


#stemming and lower-cases
from nltk.stem.snowball import SnowballStemmer
stemmer = SnowballStemmer("english")
stemmed_tokens = [stemmer.stem(t) for t in rawtokens]

# Remove stopwords
from nltk.corpus import stopwords

stemmed_tokens_no_stop = [stemmer.stem(t) for t in stemmed_tokens if t not in nltk.corpus.stopwords.words('english')]
fdist1 = nltk.FreqDist(stemmed_tokens_no_stop)
for item in fdist1.items()[:25]:
    print(item)

#print out the sorted tokens with no stop words
from __future__ import print_function
log = open("/Users/catherineordun/Desktop/sortedtokens_nostop.txt", "w")
print(sorted(stemmed_tokens_no_stop), file = log)