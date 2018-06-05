const { Given, When, Then } = require('cucumber')
const { expect } = require('chai')

Given('a variable set to {int}', (number) => {
    this.setTo(number)
})

When('I increment the variable by {int}', (number) => {
    this.incrementyBy(number)
})

Then('the variable should contain {int}', (number) => {
    expect(this.variable).to.eql(number)
})
