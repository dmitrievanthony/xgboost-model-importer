# XGBoost model importer

Allows to import XGBoost model that obtained as result of `model.dump_model('model.txt')`. The format of the dumped 
model is the following:

```
booster[0]:
0:[f29<-9.53674e-07] yes=1,no=2,missing=1
	1:[f56<-9.53674e-07] yes=3,no=4,missing=3
		3:[f60<-9.53674e-07] yes=7,no=8,missing=7
			7:[f23<-9.53674e-07] yes=13,no=14,missing=13
				13:[f24<-9.53674e-07] yes=19,no=20,missing=19
					19:leaf=1.99735
					20:leaf=-1.8
				14:leaf=-1.80952
			8:leaf=-1.95062
		4:[f21<-9.53674e-07] yes=9,no=10,missing=9
			9:leaf=1.77778
			10:leaf=-1.98104
	2:[f109<-9.53674e-07] yes=5,no=6,missing=5
		5:[f67<-9.53674e-07] yes=11,no=12,missing=11
			11:[f8<-9.53674e-07] yes=15,no=16,missing=15
				15:leaf=-1.99117
				16:leaf=1
			12:[f39<-9.53674e-07] yes=17,no=18,missing=17
				17:leaf=1.77143
				18:leaf=-1.5
		6:leaf=1.85965
booster[1]:
0:[f29<-9.53674e-07] yes=1,no=2,missing=1
	1:[f21<-9.53674e-07] yes=3,no=4,missing=3
		3:leaf=1.13207
...
```

As result of import an `XGModel` object will be returned. `XGModel` is just a function that has method `predict(obj)` and can 
be used to make predictions for new data objects:

```
double prediction = mdl.predict(testObj);
```