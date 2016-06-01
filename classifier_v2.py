# -*- coding: utf-8 -*-

"""
Created on Wed May 25 10:53:13 2016

@author: catherineordun
"""
from __future__ import print_function

from time import time
import sklearn
import numpy as np
from sklearn.datasets import load_files
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.naive_bayes import MultinomialNB
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.pipeline import Pipeline
from sklearn import metrics

#import data train files 
categories = ['Env', 'Fac', 'Med', 'Saf']
arims_train = sklearn.datasets.load_files('/Users/catherineordun/Documents/ARIMS/train',
    categories=categories, load_content=True, shuffle=True, encoding='utf-8', decode_error='ignore', random_state=42)

#allow user to enter a filenumber to see the first three lines of text
filenum = input("Enter a number: ")
print("\n".join(arims_train.data[filenum].split("\n")[:3]))
print(arims_train.target_names[arims_train.target[filenum]])

#Generating feature set: 
#tokenizing
count_vect = CountVectorizer(decode_error='ignore', strip_accents='unicode', stop_words='english')
X_train_counts = count_vect.fit_transform(arims_train.data)
feature_names = count_vect.get_feature_names()

#TF-IDF
tfidf_transformer = TfidfTransformer()
X_train_tfidf = tfidf_transformer.fit_transform(X_train_counts)

#Training model using NBM
clf = MultinomialNB().fit(X_train_tfidf, arims_train.target)

#Sort the coef_ as per feature weights and select largest 20 of them
# 2 shows that we are considering the third class
classnum = input("What are the top 20 most informative words? Enter a category number to see 0 is ENV, 1 is FAC, 2 is MED, 3 is SAF:")
inds = np.argsort(clf.coef_[classnum, :])[-20:]
for i in inds: print(feature_names [i])

#allow user to enter a sample sentence
test_sentence = raw_input("Enter a sentence, no quotes")
docs_new = [test_sentence]
X_new_counts = count_vect.transform(docs_new)
X_new_tfidf = tfidf_transformer.transform(X_new_counts)
predicted = clf.predict(X_new_tfidf)
for doc, category in zip(docs_new, predicted):
    print('%r The predicted category is => %s' % (doc, arims_train.target_names[category]))

#build Pipeline
text_clf = Pipeline([('vect', CountVectorizer()),
                     ('tfidf', TfidfTransformer()),
                     ('clf', MultinomialNB()),
])

#train the pipeline model
text_clf = text_clf.fit(arims_train.data, arims_train.target)

#import data test files
arims_test = sklearn.datasets.load_files('/Users/catherineordun/Documents/ARIMS/test',
    categories=categories, load_content=True, shuffle=True, encoding='utf-8',
    decode_error='ignore', random_state=42)
docs_test = arims_test.data
y_test = arims_test.target

# look for the largest probability and return the associated class.
print("Predicting the outcomes of the testing set...")
t0 = time()
predicted = text_clf.predict(docs_test)
print("done in %fs" % (time() - t0))
print("predicted accuracy is %f" % np.mean(predicted == arims_test.target))

#print metrics
print(metrics.classification_report(arims_test.target, predicted, target_names = arims_test.target_names))