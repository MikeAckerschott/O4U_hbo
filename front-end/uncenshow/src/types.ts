export interface User {
    id: string
    token: string
    username: string
    password: string | undefined
    image: string | undefined
}

export interface Quiz {
    name: string
    creator: User
    quiz: string
    editableQuiz: string
    img: string
    id: string
}

export interface Button {
    x: number
    y: number
    width: number
    height: number
    buttonText: string
    r: number
    g: number
    b: number
    buttonTextSize: number
    textR: number
    textG: number
    textB: number
    uniqueIndex: number
    uniqueID: string
    correct: boolean
    toDelete: boolean
}

export interface Text {
    x: number
    y: number
    width: number
    height: number
    text: string
    r: number
    g: number
    b: number
    textSize: number
    textR: number
    textG: number
    textB: number
    uniqueIndex: number
    uniqueID: string
    usingBackground: boolean
}

export interface Input {
    x: number
    y: number
    width: number
    height: number
    r: number
    g: number
    b: number
    buttonTextSize: number
    textR: number
    textG: number
    textB: number
    uniqueIndex: number
    uniqueID: string
    toDelete: boolean
    correctAnswer: string
}