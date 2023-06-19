import { configure } from 'vee-validate';
import { required, email } from '@vee-validate/rules';

export default function setupVeeValidate() {
    configure({
        validateOnInput: true,
    });

    // Add the rules you want to use
    const rules = {
        firstName: [required],
        lastName: [required],
        selectCountryCode: [required],
        cellphone: [required],
        email: [required, email],
        dateOfBirth: [required],
        currScore: [required],
    };

    // Register the rules globally
    Object.keys(rules).forEach((field) => {
        const fieldRules = rules[field];
        configure({
            generateMessage: (ctx) => {
                const messages = {
                    required: 'This field is required.',
                    email: 'Please enter a valid email address.',
                };
                const message = messages[ctx.rule.name];
                return message ? message.replace('{_field_}', ctx.field) : '';
            },
        });
        configure({
            rules: {
                [field]: fieldRules,
            },
        });
    });
}