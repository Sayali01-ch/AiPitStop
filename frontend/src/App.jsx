import React, { useEffect, useState } from 'react'

function PromptHistory({history, onSelect}){
  return (
    <div className="space-y-1">
      {history.length === 0 && <div className="text-sm text-slate-500">No history yet</div>}
      {history.map((p,idx)=> (
        <button key={idx} className="text-left w-full p-2 bg-white rounded shadow-sm hover:bg-slate-50" onClick={()=>onSelect(p)}>{p}</button>
      ))}
    </div>
  )
}

export default function App(){
  const [prompt, setPrompt] = useState('Write a short summary of AI in one paragraph');
  const [result, setResult] = useState('')
  const [loading, setLoading] = useState(false)
  const [history, setHistory] = useState([])
  const [imageData, setImageData] = useState(null)

  useEffect(()=>{
    const saved = JSON.parse(localStorage.getItem('promptHistory') || '[]')
    setHistory(saved)
  },[])

  const saveHistory = (p) => {
    const next = [p, ...history].slice(0,10)
    setHistory(next)
    localStorage.setItem('promptHistory', JSON.stringify(next))
  }

  const submitPrompt = async () => {
    setLoading(true)
    setResult('')
    setImageData(null)
    try{
      const resp = await fetch('/api/ai/text', {method: 'POST', headers: {'Content-Type': 'application/json'}, body: JSON.stringify({prompt})})
      if (!resp.ok) throw new Error('Request failed')
      const j = await resp.json()
      setResult(j.result)
      saveHistory(prompt)
    }catch(e){
      setResult('[Error] ' + e.message)
    }finally{
      setLoading(false)
    }
  }

  const generateImage = async () => {
    setLoading(true)
    setImageData(null)
    try{
      const resp = await fetch('/api/ai/image', {method: 'POST', headers: {'Content-Type': 'application/json'}, body: JSON.stringify({prompt})})
      const j = await resp.json()
      setImageData(j.image || null)
      saveHistory(prompt)
    }catch(e){
      setResult('[Error] ' + e.message)
    }finally{ setLoading(false) }
  }

  const uploadPdf = async (file) => {
    const fd = new FormData()
    fd.append('file', file)
    setLoading(true)
    try{
      const resp = await fetch('/api/doc/extract', {method: 'POST', body: fd})
      const j = await resp.json()
      alert('Extracted text length: ' + (j.text || '').length)
    }catch(e){
      alert('Upload failed: ' + e.message)
    }finally{ setLoading(false) }
  }

  return (
    <div className="max-w-4xl mx-auto p-6">
      <header className="mb-6">
        <h1 className="text-3xl font-bold">AiPitStop</h1>
        <p className="text-sm text-slate-500">Unified AI playground — test prompts, generate images, extract PDF text.</p>
      </header>

      <main className="grid md:grid-cols-3 gap-6">
        <section className="md:col-span-2 bg-white p-4 rounded shadow-sm">
          <label className="block text-sm font-medium text-slate-700">Prompt</label>
          <textarea className="mt-2 w-full p-3 border rounded" value={prompt} onChange={e=>setPrompt(e.target.value)} rows={6} />

          <div className="flex gap-3 mt-3">
            <button className="px-4 py-2 bg-indigo-600 text-white rounded" onClick={submitPrompt} disabled={loading}>{loading? 'Working...' : 'Generate Text'}</button>
            <button className="px-4 py-2 bg-emerald-600 text-white rounded" onClick={generateImage} disabled={loading}>{loading? 'Working...' : 'Generate Image'}</button>
            <label className="px-4 py-2 bg-slate-100 rounded cursor-pointer">
              <input type="file" accept="application/pdf" className="hidden" onChange={e=>uploadPdf(e.target.files[0])} />
              Upload PDF
            </label>
          </div>

          <div className="mt-4">
            <h3 className="text-sm font-semibold">Result</h3>
            <pre className="mt-2 p-3 bg-slate-50 rounded min-h-[120px] whitespace-pre-wrap">{result || 'No result yet'}</pre>
          </div>

        </section>

        <aside className="bg-white p-4 rounded shadow-sm">
          <h3 className="font-semibold mb-2">Prompt History</h3>
          <PromptHistory history={history} onSelect={(p)=>setPrompt(p)} />

          <div className="mt-4">
            <h3 className="font-semibold mb-2">Image Preview</h3>
            {imageData ? (
              <img src={imageData} alt="generated" className="w-full rounded border" />
            ) : (
              <div className="text-sm text-slate-500">No image generated yet</div>
            )}
          </div>
        </aside>
      </main>

    </div>
  )
}
