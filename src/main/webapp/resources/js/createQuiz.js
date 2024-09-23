function toggleNewCategoryField(){
    let categoryDropDown = document.querySelector("#category");
    let newCategoryField = document.querySelector("#createNewCategory");
    if(categoryDropDown.value === "newCategory"){
        newCategoryField.style.display = "block";
    }else{
        newCategoryField.style.display = "none";
    }
}

function toggleQuestionFields(){
    let quizTypeDropDown = document.querySelector("#quizType");
    let multiChoiceField = document.querySelector("#multipleChoiceAnswers");
    let trueFalseField = document.querySelector("#trueFalseFields");

    if(quizTypeDropDown.value === "multi"){
        trueFalseField.style.display = "none";
        multiChoiceField.style.display = "block";
    }else{
        multiChoiceField.style.display = "none";
        trueFalseField.style.display = "block";
    }
}


