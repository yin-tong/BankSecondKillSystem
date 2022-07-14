import { validPhone, isExternal } from '@/utils/validate.js'

describe('Utils:validate', () => {
  it('validPhone', () => {
    expect(validPhone('admin')).toBe(true)
    expect(validPhone('editor')).toBe(true)
    expect(validPhone('xxxx')).toBe(false)
  })
  it('isExternal', () => {
    expect(isExternal('https://github.com/PanJiaChen/vue-element-admin')).toBe(true)
    expect(isExternal('http://github.com/PanJiaChen/vue-element-admin')).toBe(true)
    expect(isExternal('github.com/PanJiaChen/vue-element-admin')).toBe(false)
    expect(isExternal('/dashboard')).toBe(false)
    expect(isExternal('./dashboard')).toBe(false)
    expect(isExternal('dashboard')).toBe(false)
  })
})
