import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import EmailSender from './components/EmailSender'
import { Toaster } from 'react-hot-toast'

function App() {

  return (
    <>
     <Toaster/>
     <EmailSender/>
    </>
  )
}

export default App
