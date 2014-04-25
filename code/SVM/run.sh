#Run the file from data
java -cp "../../lib/*:./Implementation/bin/" util.SVMPreprocessor $1

# Get C parameter
C=$(sed '3q;d' DATA.TXT | cut -d "=" -f 2)
echo $C
# Learn the SVM
./Implementation/svm_learn -c $C ./Implementation/svmTrain.txt ./Implementation/model 
./Implementation/svm_classify ./Implementation/svmTest.txt ./Implementation/model  ./Implementation/predictions > /dev/null
cat ./Implementation/predictions