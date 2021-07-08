$.getJSON("/questions.json", function(json) {
    function buildQuiz() {
        // we'll need a place to store the HTML output
        const output = [];

        // for each question...
        json.forEach((currentQuestion, questionNumber) => {
            // we'll want to store the list of answer choices
            const choices = [];

        // and for each available answer...
        for (letter in currentQuestion.choices) {
            // ...add an HTML radio button
            choices.push(
                `<label>
             <input type="radio" name="question${questionNumber}" value="${letter}">
              ${currentQuestion.choices[letter]}
           </label>`
            );
        }

        // add this question and its answers to the output
        output.push(
            `<div class="slide">
           <div class="question"> ${currentQuestion.question} </div>
           <div class="slogan"> ${currentQuestion.slogan} </div>
           <div class="answers"> ${choices.join("")} </div>
         </div>`
        );
    });

        // finally combine our output list into one string of HTML and put it on the page
        quizContainer.innerHTML = output.join("");
    }

    function showResults() {
        // gather answer containers from our quiz
        const answerContainers = quizContainer.querySelectorAll(".answers");

        // keep track of user's answers
        let totalValue = 0;

        // for each question...
        json.forEach((currentQuestion, questionNumber) => {
            // find selected answer
            const answerContainer = answerContainers[questionNumber];
        const selector = `input[name=question${questionNumber}]:checked`;
        const userAnswer = (answerContainer.querySelector(selector) || {}).value;

        totalValue += parseInt(userAnswer);
    });

        // show number of correct answers out of total
        var mailbody;
        if(totalValue <= 20) {
            mailbody = `My dear AppWorks friend,\nYou reached a total of ${totalValue} points out of the max. ${json.length * 10} points.\nOeps...Do you really want to be the AppWorks guy?`;
        }
        if(totalValue > 20 && totalValue <= 40 ) {
            mailbody = `My dear AppWorks friend,\nYou reached a total of ${totalValue} points out of the max. ${json.length * 10} points.\nHmmm...You should work on your skills, but you are on the right track by landing on this site.`;
        }
        if(totalValue > 40 && totalValue <= 60) {
            mailbody = `My dear AppWorks friend,\nYou reached a total of ${totalValue} points out of the max. ${json.length * 10} points.\nCool...Just do the extra bit of work to get closer to your target!`;
        }
        if(totalValue > 60 && totalValue <= 80) {
            mailbody = `My dear AppWorks friend,\nYou reached a total of ${totalValue} points out of the max. ${json.length * 10} points.\nWow...You're getting somewhere so keep up the good work.`;
        }
        if(totalValue > 80 && totalValue <= 100) {
            mailbody = `My dear AppWorks friend,\nYou reached a total of ${totalValue} points out of the max. ${json.length * 10} points.\nHell yeah!...We should be friends as you have your nose pointed to the best-of-all direction.`;
        }
        resultsContainer.innerHTML = mailbody;
    }

    function startQuiz() {
        showSlide(0);
        startButton.style.display = "none";
        subtitleContainer.style.display = "none";
        progress.style.display = "inline-block";
    }

    function showSlide(n) {
        progress.setAttribute("value", (n+1)*10);

        slides[currentSlide].classList.remove("active-slide");
        slides[n].classList.add("active-slide");
        currentSlide = n;

        if (currentSlide === slides.length - 1) {
            nextButton.style.display = "none";
            finalizeButton.style.display = "inline-block";
        } else {
            nextButton.style.display = "inline-block";
        }
    }

    function showNextSlide() {
        const answersForCurrentQuestion = quizContainer.querySelectorAll(".answers")[currentSlide];
        let answerGiven = false;
        answersForCurrentQuestion.querySelectorAll("input").forEach((input, index) => {
            // console.log(index, input);
            if(input.checked) {
                showSlide(currentSlide + 1);
                answerGiven = true;
                errorContainer.innerText = "";
            }
        });
        if(!answerGiven) {
            errorContainer.innerText = "Please make a selection!";
        }
    }

    function finalizeQuiz() {
        const answersForCurrentQuestion = quizContainer.querySelectorAll(".answers")[currentSlide];
        let answerGiven = false;
        answersForCurrentQuestion.querySelectorAll("input").forEach((input, index) => {
            // console.log(index, input);
            if(input.checked) {
                submitForm.style.display = "inline";
                finalizeButton.style.display = "none";
                quizcontainerContainer.style.display = "none";
                answerGiven = true;
                errorContainer.innerText = "";
                showResults();
            }
        });
        if(!answerGiven) {
            errorContainer.innerText = "Please make a selection!";
        }
    }

    const subtitleContainer = document.getElementById("subtitle");
    const quizContainer = document.getElementById("quizdiv");
    const quizcontainerContainer = document.getElementById("quizcontainer");
    const resultsContainer = document.getElementById("id-iFB17EMSOH");
    resultsContainer.style.display = "none";
    const errorContainer = document.getElementById("error");
    const submitButton = document.getElementById("submit");
    const finalizeButton = document.getElementById("finalize");
    finalizeButton.style.display = "none";
    const submitForm = document.getElementById("submitform");
    submitForm.style.display = "none";
    const startButton = document.getElementById("start");

    // display quiz right away
    buildQuiz();

    const nextButton = document.getElementById("next");
    nextButton.style.display = "none";
    const progress = document.getElementById("progress");
    progress.style.display = "none";
    const slides = document.querySelectorAll(".slide");
    let currentSlide = 0;

    nextButton.addEventListener("click", showNextSlide);
    startButton.addEventListener("click", startQuiz);
    finalizeButton.addEventListener("click", finalizeQuiz);
});

$("#mail").keyup(function(){
    $("#cc").val($("#mail").val());
});

setTimeout( function(){
    var myCookie = getCookie("appworks_tips");

    if (myCookie == null) {
        setCookie('appworks_tips', 'quiz_ask', 30);
        if(window.location.pathname === '/') {
            window.location.href = "/#quizask";
        }
    }
    else {
        // do cookie exists stuff
    }
}  , 3000 );

function getCookie(name) {
    var match = document.cookie.match(RegExp('(?:^|;\\s*)' + name + '=([^;]*)'));
    return match ? match[1] : null;
}

function setCookie(cName, cValue, expDays) {
    let date = new Date();
    date.setTime(date.getTime() + (expDays * 24 * 60 * 60 * 1000));
    const expires = "expires=" + date.toUTCString();
    document.cookie = cName + "=" + cValue + "; " + expires + "; path=/";
}

function handleNo() {
    window.location.href = "/#";
}

function handleYes() {
    window.location.href = "/#quiz";
}
