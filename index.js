console.log(1)

function resetPair(arr){
  const z = arr
  let index = 0

  const pair = {
    a: Number.MAX_VALUE,
    b: 0,
  }

  for(let i=0; i<z.length-1; i++){
    if(z[i]+z[i+1] < pair.a + pair.b){
      pair.a = z[i]
      pair.b = z[i+1]
      index = i
    }
  }

  z.splice(index, 1)
  z.splice(index, 1)

  z.splice(index, 0,  pair.a+pair.b)
  return pair.a+pair.b
}

function solve(arr){
  let result = 0
  
  while(arr.length!==1){
    result+=resetPair(arr)
  }

  return result
}


console.log(solve([4, 1, 5, 7]))
