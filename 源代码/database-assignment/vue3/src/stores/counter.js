import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
export const useCounterStore
    = defineStore('counter', () => {
  const count = ref(0)
  const test = ref("成功!")
  const currentPathName = ref('')
  const user = {}
  const doubleCount = computed(() => count.value * 2)
  function increment() {
    console.log(count.value)
    count.value++
  }
  function setPath(state) {
    this.currentPathName = localStorage.getItem("currentPathName")
  }
  return { count, doubleCount, increment, setPath, test}
})
